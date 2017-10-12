package com.paul.cruz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.paul.cruz.Utils.HelperClass;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText email_et, password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.signin_button).setOnClickListener(this);
        email_et = (TextInputEditText) findViewById(R.id.email_et);
        password_et = (TextInputEditText) findViewById(R.id.password_et);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.signin_button:
                Intent intent = new Intent(this, MainScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    private boolean checkForEmpty() {

        boolean flag = false;
        if (email_et.getText().toString().isEmpty()) {
            email_et.setError(getString(R.string.enter_a_email));
            flag = true;
        } else {
            if (!HelperClass.isEmailValid(email_et.getText().toString())) {
                email_et.setError(getString(R.string.enter_a_valid_email));
                flag = true;
            }
        }
        if (password_et.getText().toString().isEmpty()) {
            password_et.setError(getString(R.string.enter_password));
            flag = true;
        }
        if (flag)
            return false;
        else return true;
    }

}
