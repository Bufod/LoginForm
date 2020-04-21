package com.example.loginform;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText loginField, passwordField;
    DBServer dbServer;
    public static DBServer.Users users;
    Button signIn;
    User currentUser;
    TextView signUp;
    private static final int ADD_USER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbServer = new DBServer(this);
        users = dbServer.new Users();

        loginField = findViewById(R.id.loginField);
        passwordField = findViewById(R.id.passwordField);
        signIn = findViewById(R.id.signInBt);
        signIn.setOnClickListener(signInClick());

        signUp = findViewById(R.id.signUpTxt);
        signUp.setOnClickListener(signUpClick());
    }

    private View.OnClickListener signUpClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        RegistrationActivity.class);
                startActivityForResult(intent, ADD_USER);
            }
        };
    }

    private View.OnClickListener signInClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText[] fields = new EditText[]{loginField, passwordField};
                if (ValidationField.validateEditText(fields, MainActivity.this)){
                    if (authentication(
                            loginField.getText().toString(),
                            passwordField.getText().toString())){
                        Intent intent = new Intent(MainActivity.this,
                                HelloActivity.class);
                        intent.putExtra(HelloActivity.USER_BUNDLE, currentUser);
                        startActivity(intent);
                    } else {
                        ValidationField.setErrorStateEditText(fields, "Некорректные данные",
                                MainActivity.this);
                    }
                }
            }
        };
    }

    private boolean authentication(String login, String password){
        return (currentUser = users.select(login, password)) != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_USER && resultCode == RESULT_OK){
            if (data != null && data.hasExtra(RegistrationActivity.USER_BUNDLE)){
                currentUser = (User) data.getSerializableExtra(RegistrationActivity.USER_BUNDLE);
                if (currentUser != null)
                    users.insert(currentUser);
            }
        }
    }
}
