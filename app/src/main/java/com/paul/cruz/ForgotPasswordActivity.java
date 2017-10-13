package com.paul.cruz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.paul.cruz.Utils.Endpoints;
import com.paul.cruz.Utils.HelperClass;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText email_et;
    Button reset_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        email_et = (TextInputEditText) findViewById(R.id.email_et);
        reset_button = (Button) findViewById(R.id.reset_button);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_et.getText().toString().isEmpty()) {
                    email_et.setError(getString(R.string.enter_a_email));
                } else {
                    if (!HelperClass.isEmailValid(email_et.getText().toString())) {
                        email_et.setError(getString(R.string.enter_a_valid_email));
                    } else {
                        resetPassword();
                    }
                }
            }
        });
    }

    private void resetPassword() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();
        final JsonObject json = new JsonObject();
        json.addProperty("email", email_et.getText().toString());

        Ion.with(this)
                .load(Endpoints.FORGOT_PASSWORD_URL)
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

                        } else {

                        }
                    }
                });
    }
}
