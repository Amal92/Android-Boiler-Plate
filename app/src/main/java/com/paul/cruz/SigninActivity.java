package com.paul.cruz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.paul.cruz.Utils.Const;
import com.paul.cruz.Utils.Endpoints;
import com.paul.cruz.Utils.HelperClass;
import com.paul.cruz.Utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText email_et, password_et;

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
        findViewById(R.id.forgot_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.signin_button:
                if (checkForEmpty())
                    login();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void login() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        final JsonObject json = new JsonObject();
        json.addProperty("username", email_et.getText().toString());
        json.addProperty("password", password_et.getText().toString());
        json.addProperty("client_id", "2");
        json.addProperty("client_secret", getResources().getString(R.string.client_secret_key));
        json.addProperty("provider", "users");
        json.addProperty("scope", "");
        json.addProperty("grant_type", "password");

        Ion.with(this)
                .load(Endpoints.LOGIN_URL)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        progressDialog.dismiss();
                        if (result == null)
                            return;
                        if (result.getHeaders().code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                SharedPreferencesUtils.setParam(SigninActivity.this, SharedPreferencesUtils.SESSION_TOKEN, jsonObject.optString(Const.access_token));
                                Intent intent = new Intent(SigninActivity.this, MainScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                Toast.makeText(SigninActivity.this, jsonObject.optString(Const.message), Toast.LENGTH_LONG).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
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
        } else if (password_et.getText().length() < 6) {
            password_et.setError(getString(R.string.valid_password));
            flag = true;
        }
        if (flag)
            return false;
        else return true;
    }

}
