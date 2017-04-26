package com.project.korsa.korsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    Switch riderOrDriverSwitch;

    public void getStarted(View view) {

        String riderOrDriver = "rider";

        if (riderOrDriverSwitch.isChecked()) {

            riderOrDriver = "driver";

        }

        ParseUser.getCurrentUser().put("riderOrDriver", riderOrDriver);

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                    redirectUser();

                }
            }
        });
    }

    public void redirectUser() {

        if (ParseUser.getCurrentUser().get("riderOrDriver").equals("rider")) {

            Intent i = new Intent(getApplicationContext(), YourLocation.class);
            startActivity(i);

        } else {

            Intent i = new Intent(getApplicationContext(), ViewRequests.class);
            startActivity(i);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ParseUser.getCurrentUser().put("riderOrDriver", "rider");

        riderOrDriverSwitch = (Switch) findViewById(R.id.riderOrDriverSwitch);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        getSupportActionBar().hide();

        if (ParseUser.getCurrentUser() == null) {

            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.d("MyApp", "Anonymous login failed.");
                    } else {
                        Log.d("MyApp", "Anonymous user logged in.");
                    }
                }
            });

        } /*else {

            if (ParseUser.getCurrentUser().get("riderOrDriver") != null) {

                redirectUser();

            }
        }*/
    }
}
