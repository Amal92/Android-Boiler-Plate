package com.paul.cruz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.paul.cruz.Utils.SharedPreferencesUtils;

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
        String session_token = (String) SharedPreferencesUtils.getParam(this,SharedPreferencesUtils.SESSION_TOKEN,"");
        if (!session_token.equals("")){
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
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
