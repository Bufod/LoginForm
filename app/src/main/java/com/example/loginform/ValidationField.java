package com.example.loginform;


import android.content.Context;
import android.widget.EditText;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class ValidationField {

    public static String encryptString(String unencStr){
        return BCrypt.hashpw(unencStr, BCrypt.gensalt(12));
    }

    public static void setErrorStateEditText(EditText[] editTexts, String errorMsg,
                                             Context context){
        for(EditText editText : editTexts){
            editText.setBackground(context.getDrawable(R.drawable.bkg_error_edit_text));
            editText.setError(errorMsg);
        }
    }

    public static boolean validateEditText(EditText[] editTexts, Context context){
        boolean check = true;
        ArrayList<EditText> errorFields = new ArrayList<>(editTexts.length);
        for (EditText editText : editTexts){
            if (editText.getText() == null ||
                    (editText.getText() != null &&
                            editText.getText().toString().equals(""))){
                errorFields.add(editText);
                check = false;
            } else {
                editText.setBackground(context.getDrawable(R.drawable.bkg_normal_edit_text));
            }
        }
        setErrorStateEditText(errorFields.toArray(new EditText[0]),
                "Обязательное поле", context);

        return check;
    }
}
