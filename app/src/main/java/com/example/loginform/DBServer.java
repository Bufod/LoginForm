package com.example.loginform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class DBServer {
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    public DBServer(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public class Users {
        private static final String TABLE_NAME = "users";

        private static final String COLUMN_ID = "id";
        private static final String COLUMN_LOGIN = "login";
        private static final String COLUMN_PASSWORD = "password";
        private static final String COLUMN_FIRSTNAME = "firstname";
        private static final String COLUMN_LASTNAME = "lastname";
        private static final String COLUMN_ADDRESS = "address";
        private static final String COLUMN_SEX = "sex";

        private static final int NUM_COLUMN_ID = 0;
        private static final int NUM_COLUMN_LOGIN = 1;
        private static final int NUM_COLUMN_PASSWORD = 2;
        private static final int NUM_COLUMN_FIRSTNAME = 3;
        private static final int NUM_COLUMN_LASTNAME = 4;
        private static final int NUM_COLUMN_ADDRESS = 5;
        private static final int NUM_COLUMN_SEX = 6;

        public boolean insert(String login, String password, String firstname,
                              String lastname, String address, Integer sex) {
            String sql = "INSERT INTO " + TABLE_NAME + " ("
                    + COLUMN_LOGIN + ",\n"
                    + COLUMN_PASSWORD + ",\n"
                    + COLUMN_FIRSTNAME + ",\n"
                    + COLUMN_LASTNAME + ",\n"
                    + COLUMN_ADDRESS + ",\n"
                    + COLUMN_SEX + "\n"
                    + ") VALUES (?, ?, ?, ?, ?, ?)";

            SQLiteStatement stmt = database.compileStatement(sql);
            stmt.bindString(1, login);
            stmt.bindString(2, password);
            stmt.bindString(3, firstname);
            stmt.bindString(4, lastname);
            stmt.bindString(5, address);
            stmt.bindLong(6, sex);

            long value = stmt.executeInsert();
            stmt.close();
            return value > 0;
        }

        public boolean insert(User user) {
            return insert(user.getLogin(), user.getPassword(), user.getFirstname(),
                    user.getLastname(), user.getAddress(), user.getIntSex());
        }

        public boolean checkLogin(String login) {
            Cursor cursor = database.query(TABLE_NAME, null,
                    COLUMN_LOGIN + " = ?", new String[]{login}, null,
                    null, null);
            boolean checkOk = !(cursor.getCount() > 0);
            cursor.close();
            return checkOk;
        }

        public int update(User user) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_LOGIN, user.getLogin());
            if (user.getPassword() != null)
                cv.put(COLUMN_PASSWORD, user.getPassword());
            cv.put(COLUMN_FIRSTNAME, user.getFirstname());
            cv.put(COLUMN_LASTNAME, user.getLastname());
            cv.put(COLUMN_ADDRESS, user.getAddress());
            cv.put(COLUMN_SEX, user.getIntSex());
            return database.update(TABLE_NAME, cv, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.getId())});
        }

        public void deleteAll() {
            database.delete(TABLE_NAME, null, null);
        }

        public void delete(User user) {
            database.delete(TABLE_NAME, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.getId())});
        }

        public User select(String login, String password) {
            Cursor cursor = database.query(TABLE_NAME, null,
                    COLUMN_LOGIN + " = ?", new String[]{login}, null,
                    null, null);
            User user = null;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                if (BCrypt.checkpw(password, cursor.getString(NUM_COLUMN_PASSWORD))) {
                    int id = cursor.getInt(NUM_COLUMN_ID);
                    String firstname = cursor.getString(NUM_COLUMN_FIRSTNAME);
                    String lastname = cursor.getString(NUM_COLUMN_LASTNAME);
                    String address = cursor.getString(NUM_COLUMN_ADDRESS);
                    Integer sex = cursor.getInt(NUM_COLUMN_SEX);
                    user = new User(id, login, firstname, lastname, address, sex);
                }
            }
            cursor.close();
            return user;
        }

        public ArrayList<User> selectAll() {
            Cursor cursor = database.query(TABLE_NAME, null,
                    null, null, null,
                    null, null);

            ArrayList<User> arr = new ArrayList<>();
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    int id = cursor.getInt(NUM_COLUMN_ID);
                    String login = cursor.getString(NUM_COLUMN_LOGIN);
                    String firstname = cursor.getString(NUM_COLUMN_FIRSTNAME);
                    String lastname = cursor.getString(NUM_COLUMN_LASTNAME);
                    String address = cursor.getString(NUM_COLUMN_ADDRESS);
                    Integer sex = cursor.getInt(NUM_COLUMN_SEX);
                    arr.add(new User(id, login, firstname, lastname, address, sex));
                } while (cursor.moveToFirst());

            }
            cursor.close();
            return arr;
        }
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String dbQuery = "CREATE TABLE " + Users.TABLE_NAME + " (" +
                    Users.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Users.COLUMN_LOGIN + " TEXT UNIQUE NOT NULL, " +
                    Users.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    Users.COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                    Users.COLUMN_LASTNAME + " TEXT NOT NULL, " +
                    Users.COLUMN_ADDRESS + " TEXT, " +
                    Users.COLUMN_SEX + " INTEGER NOT NULL);";
            sqLiteDatabase.execSQL(dbQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

}
