package com.paul.cruz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;

import com.paul.cruz.Utils.HelperClass;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText name_et, email_et, phone_et,password_et;
    private AppCompatRadioButton male_rd, female_rd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.signup_button).setOnClickListener(this);
        name_et = (TextInputEditText) findViewById(R.id.name_et);
        email_et = (TextInputEditText) findViewById(R.id.email_et);
        phone_et = (TextInputEditText) findViewById(R.id.phone_et);
        password_et = (TextInputEditText) findViewById(R.id.password_et);
        male_rd = (AppCompatRadioButton) findViewById(R.id.male_rd);
        female_rd = (AppCompatRadioButton) findViewById(R.id.female_rd);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.signup_button:
                Intent intent = new Intent(this, MainScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    private boolean checkForEmpty() {
        boolean flag = false;
        if (name_et.getText().toString().isEmpty()) {
            name_et.setError(getString(R.string.enter_a_name));
            flag = true;
        }
        if (email_et.getText().toString().isEmpty()) {
            email_et.setError(getString(R.string.enter_a_email));
            flag = true;
        } else {
            if (!HelperClass.isEmailValid(email_et.getText().toString())) {
                email_et.setError(getString(R.string.enter_a_valid_email));
                flag = true;
            }
        }
        if (phone_et.getText().toString().isEmpty()) {
            phone_et.setError(getString(R.string.enter_phone_no));
            flag = true;
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
