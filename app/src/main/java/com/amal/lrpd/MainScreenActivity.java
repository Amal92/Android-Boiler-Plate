package com.amal.lrpd;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.amal.lrpd.Fragments.MapFragment;
import com.amal.lrpd.Utils.Const;
import com.amal.lrpd.Utils.Endpoints;
import com.amal.lrpd.Utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView name;
    TextView email;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        name = (TextView) headerLayout.findViewById(R.id.name);
        email = (TextView) headerLayout.findViewById(R.id.email);
        profile_image = (ImageView) headerLayout.findViewById(R.id.profile_image);
        LinearLayout header_ll = (LinearLayout) headerLayout.findViewById(R.id.header_ll);
        header_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreenActivity.this, ProfileActivity.class));
            }
        });

        gotoMapFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
    }

    private void gotoMapFragment() {
        MapFragment newFragment = new MapFragment();
            /*if (category != null) {
                Bundle args = new Bundle();
                args.putString(Const.ITEM_ID_TO_SEARCH, category.get_ID());
                args.putString(Const.ITEM_NAME_TO_SEARCH, category.getNAME());
                newFragment.setArguments(args);
            }*/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.screen_container, newFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getProfile() {

        Ion.with(this)
                .load(Endpoints.PROFILE_URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", "Bearer " + SharedPreferencesUtils.getParam(MainScreenActivity.this, SharedPreferencesUtils.SESSION_TOKEN, ""))
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (result == null) {
                            return;
                        }
                        if (result.getHeaders().code() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult());
                                name.setText(jsonObject.optString("name"));
                                email.setText(jsonObject.optString("email"));
                                Picasso.with(MainScreenActivity.this)
                                        .load(jsonObject.optString("avatar"))
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(profile_image);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult().toString());
                                Toast.makeText(MainScreenActivity.this, jsonObject.optString(Const.message), Toast.LENGTH_LONG).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // erase all pref and go to start screen
                        erasePref();
                        Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.pink));
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.pink));
    }

    private void erasePref() {
        SharedPreferencesUtils.setParam(this, SharedPreferencesUtils.SESSION_TOKEN, "");
    }
}
