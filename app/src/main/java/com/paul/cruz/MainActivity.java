package com.paul.cruz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.signin_button).setOnClickListener(this);
        findViewById(R.id.signup_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_button:
                startActivity(new Intent(this, SigninActivity.class));
                break;
            case R.id.signup_button:
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }
}
