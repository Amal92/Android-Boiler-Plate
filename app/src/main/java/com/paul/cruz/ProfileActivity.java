package com.paul.cruz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.paul.cruz.Utils.Const;
import com.paul.cruz.Utils.Endpoints;
import com.paul.cruz.Utils.HelperClass;
import com.paul.cruz.Utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfileActivity extends AppCompatActivity {

    private final int PICK_IMAGE = 12345;
    private Bitmap bitmap;

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
                    if (bitmap == null)
                        updateProfile();
                    else {
                        updateProfile(1);
                    }
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
        getProfile();
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
        if (flag)
            return false;
        else return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    profile_image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        String gender = "";
        if (male_rd.isChecked())
            gender = "M";
        else gender = "F";
        final JsonObject json = new JsonObject();
        json.addProperty("email", email_et.getText().toString());
        json.addProperty("name", name_et.getText().toString());
        json.addProperty("mobile", phone_et.getText().toString());
        json.addProperty("device_type", "android");
        json.addProperty("device_token", "token");
        json.addProperty("device_id", "id");
        json.addProperty("gender", gender);

        Ion.with(this)
                .load(Endpoints.UPDATE_PROFILE_URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", "Bearer " + SharedPreferencesUtils.getParam(ProfileActivity.this, SharedPreferencesUtils.SESSION_TOKEN, ""))
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
                            Toast.makeText(ProfileActivity.this, "Updated successfully", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                Toast.makeText(ProfileActivity.this, jsonObject.optString(Const.message), Toast.LENGTH_LONG).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;
    }


    private void updateProfile(int x) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        File imageFile = persistImage(bitmap, "profile");
        String gender = "";
        if (male_rd.isChecked())
            gender = "M";
        else gender = "F";
        Ion.with(this)
                .load(Endpoints.UPDATE_PROFILE_URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", "Bearer " + SharedPreferencesUtils.getParam(ProfileActivity.this, SharedPreferencesUtils.SESSION_TOKEN, ""))
                .setMultipartParameter("email", email_et.getText().toString())
                .setMultipartParameter("name", name_et.getText().toString())
                .setMultipartParameter("mobile", phone_et.getText().toString())
                .setMultipartParameter("device_type", "android")
                .setMultipartParameter("device_token", "token")
                .setMultipartParameter("device_id", "id")
                .setMultipartParameter("gender", gender)
                .setMultipartFile("avatar", "image/jpeg", imageFile)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        progressDialog.dismiss();
                        if (result == null)
                            return;
                        if (result.getHeaders().code() == 200) {
                            Toast.makeText(ProfileActivity.this, "Updated successfully", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                Toast.makeText(ProfileActivity.this, jsonObject.optString(Const.message), Toast.LENGTH_LONG).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void getProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Ion.with(this)
                .load(Endpoints.PROFILE_URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", "Bearer " + SharedPreferencesUtils.getParam(ProfileActivity.this, SharedPreferencesUtils.SESSION_TOKEN, ""))
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        progressDialog.dismiss();
                        if (result == null) {
                            return;
                        }
                        if (result.getHeaders().code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult());
                                name_et.setText(jsonObject.optString("name"));
                                email_et.setText(jsonObject.optString("email"));
                                phone_et.setText(jsonObject.optString("mobile"));
                                Picasso.with(ProfileActivity.this)
                                        .load(jsonObject.optString("avatar"))
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(profile_image);
                                if (jsonObject.optString("gender").equalsIgnoreCase("m")) {
                                    male_rd.setChecked(true);
                                } else if (jsonObject.optString("gender").equalsIgnoreCase("f")) {
                                    female_rd.setChecked(true);
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                Toast.makeText(ProfileActivity.this, jsonObject.optString(Const.message), Toast.LENGTH_LONG).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }


}
