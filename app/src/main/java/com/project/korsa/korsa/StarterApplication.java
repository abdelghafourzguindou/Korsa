package com.project.korsa.korsa;

/**
 * Created by zguindouos on 15/04/17.
 */

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("KorsaApp")
                .clientKey(null)
                //.server("http://192.168.43.34:1337/parse/")   //Without proxy
                .server("http://10.23.21.222:1337/parse/")    //With proxy
                //.server("http://localhost:1337/parse/")       //Localhost unused
                //.server("http://10.0.2.2:1337/parse/")        //Localhost for emulator
                .build());
        //Parse.initialize(this);
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}