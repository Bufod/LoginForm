package com.example.loginform;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloActivity extends AppCompatActivity {

    public static final String USER_BUNDLE = "Current User";
    TextView loginTw, firstAndLastNameTw, addressTw, sexTw;
    ImageView icon;
    Button editInfoBt;
    User currentUser = null;
    DBServer.Users users = MainActivity.users;
    private static final int EDIT_USER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        firstAndLastNameTw = findViewById(R.id.firstAndLastNameTw);
        addressTw = findViewById(R.id.addressTw);
        sexTw = findViewById(R.id.sexTw);
        loginTw = findViewById(R.id.loginTw);
        icon = findViewById(R.id.icon);
        editInfoBt = findViewById(R.id.editInfoBt);
        editInfoBt.setOnClickListener(editInfoBtClick());

        if (getIntent().hasExtra(USER_BUNDLE)){
            currentUser = (User) getIntent().getSerializableExtra(USER_BUNDLE);
            fillUserInfo(currentUser);
        }

    }

    private void fillUserInfo(User currentUser) {
        if (currentUser != null){
            String tmpStr = currentUser.getFirstname() + " " + currentUser.getLastname();
            firstAndLastNameTw.setText(tmpStr);
            addressTw.setText(currentUser.getAddress());
            sexTw.setText(currentUser.getSex());
            loginTw.setText(currentUser.getLogin());
            if (currentUser.getSex().equals(User.SEX_MAN))
                icon.setImageDrawable(getDrawable(R.drawable.man));
            else
                icon.setImageDrawable(getDrawable(R.drawable.woman));
        }
    }

    private View.OnClickListener editInfoBtClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelloActivity.this,
                        RegistrationActivity.class);

                intent.putExtra(RegistrationActivity.USER_BUNDLE, currentUser);
                startActivityForResult(intent, EDIT_USER);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_USER && resultCode == RESULT_OK){
            if (data != null && data.hasExtra(RegistrationActivity.USER_BUNDLE)){
                currentUser = (User) data.getSerializableExtra(RegistrationActivity.USER_BUNDLE);
                if (currentUser != null){
                    users.update(currentUser);
                    fillUserInfo(currentUser);
                }
            }
        }
    }
}
