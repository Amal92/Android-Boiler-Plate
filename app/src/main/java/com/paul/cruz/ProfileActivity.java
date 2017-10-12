package com.paul.cruz;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.paul.cruz.Utils.HelperClass;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profile_image;
    private TextInputEditText name_et, email_et, phone_et;
    private AppCompatRadioButton male_rd, female_rd;
    private Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        profile_image = (ImageView) findViewById(R.id.profile_image);
        name_et = (TextInputEditText) findViewById(R.id.name_et);
        email_et = (TextInputEditText) findViewById(R.id.email_et);
        phone_et = (TextInputEditText) findViewById(R.id.phone_et);
        male_rd = (AppCompatRadioButton) findViewById(R.id.male_rd);
        female_rd = (AppCompatRadioButton) findViewById(R.id.female_rd);
        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForEmpty()) {

                }
            }
        });
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
        if (flag)
            return false;
        else return true;
    }


}
