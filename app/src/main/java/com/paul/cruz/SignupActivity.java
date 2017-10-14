package com.paul.cruz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.paul.cruz.Utils.Endpoints;
import com.paul.cruz.Utils.HelperClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText name_et, email_et, phone_et, password_et;
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
                if (checkForEmpty()) {
                    register();
                }
                break;
        }
    }

    private void register() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        final JsonObject json = new JsonObject();
        json.addProperty("email", email_et.getText().toString());
        json.addProperty("password", password_et.getText().toString());
        json.addProperty("name", name_et.getText().toString());
        json.addProperty("mobile", phone_et.getText().toString());
        json.addProperty("device_type", "android");
        json.addProperty("device_token", "token");
        json.addProperty("device_id", "id");
        if (male_rd.isChecked()) {
            json.addProperty("gender", "M");
        } else {
            json.addProperty("gender", "F");
        }

        Ion.with(this)
                .load(Endpoints.REGISTER_URL)
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

                            Toast.makeText(SignupActivity.this, "Successfully registered. Please sign in to continue.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                JSONObject errorObject = jsonObject.getJSONObject("errors");
                                JSONArray emailArray = errorObject.optJSONArray("email");
                                StringBuilder stringBuilder = new StringBuilder("");
                                if (emailArray != null) {
                                    for (int i = 0; i < emailArray.length(); i++) {
                                        stringBuilder.append(emailArray.get(i) + " ");
                                    }
                                    Toast.makeText(SignupActivity.this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
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
