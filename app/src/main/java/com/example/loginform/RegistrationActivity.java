package com.example.loginform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class RegistrationActivity extends AppCompatActivity {

    public static final String USER_BUNDLE = "Current User";

    RadioGroup sexRGroup;
    Button okBt, closeBt;
    EditText firstname,
            lastname,
            address,
            login,
            password;
    User currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstname = findViewById(R.id.firstNameField);
        lastname = findViewById(R.id.lastNameField);
        login = findViewById(R.id.loginField);
        password = findViewById(R.id.passwordField);
        address = findViewById(R.id.addressField);
        sexRGroup = findViewById(R.id.sexRGroup);
        okBt = findViewById(R.id.okBt);
        okBt.setOnClickListener(btClick());

        closeBt = findViewById(R.id.closeBt);
        closeBt.setOnClickListener(btClick());

        if (getIntent().hasExtra(USER_BUNDLE)){
            currentUser = (User) getIntent().getSerializableExtra(USER_BUNDLE);
            if (currentUser != null){
                firstname.setText(currentUser.getFirstname());
                lastname.setText(currentUser.getLastname());
                address.setText(currentUser.getAddress());
                login.setText(currentUser.getLogin());
                password.setHint("Новый пароль");
                int sexId = currentUser.getSex().equals(User.SEX_MAN) ? R.id.maleRBt :
                        R.id.femaleRBt;
                sexRGroup.check(sexId);
                okBt.setText("Сохранить");
            }
        }
    }

    private View.OnClickListener btClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == okBt){
                    EditText[] checkFields = null;
                    if (currentUser != null)
                        checkFields = new EditText[]{firstname, lastname, address, login};
                    else
                        checkFields = new EditText[]{firstname, lastname, address, login, password};
                    if (ValidationField.validateEditText(checkFields,
                            RegistrationActivity.this)){
                        String loginStr = login.getText().toString();
                        boolean correctLogin = MainActivity.users.checkLogin(loginStr);
                        if ((!correctLogin && currentUser != null &&
                                !currentUser.getLogin().equals(loginStr)) ||
                                (currentUser == null && !correctLogin)){
                            ValidationField.setErrorStateEditText(new EditText[]{login},
                                    "Логин занят",
                                    RegistrationActivity.this);
                        }
                        else {
                            int sex = sexRGroup.getCheckedRadioButtonId() == R.id.maleRBt ? 1 : 0;
                            Integer id = null;
                            if (currentUser != null)
                                id = currentUser.getId();
                            String passwordStr = password.getText().toString();
                            if (!passwordStr.equals(""))
                                passwordStr = ValidationField.encryptString(passwordStr);
                            else
                                passwordStr = null;

                            User user = new User(id, loginStr, passwordStr,
                                    firstname.getText().toString(), lastname.getText().toString(),
                                    address.getText().toString(), sex);

                            Intent intent = getIntent();
                            intent.putExtra(USER_BUNDLE, user);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                } else if (view == closeBt) {
                    finish();
                }
            }
        };
    }
}
