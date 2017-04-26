package com.project.korsa.korsa;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.location.LocationListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ViewRequests extends AppCompatActivity implements LocationListener {

    final int PERMISSION_LOCATION_REQUEST_CODE = 5954;

    ListView listView;
    ArrayList<String> listViewContent;
    ArrayList<ParseObject> requests;
    ArrayAdapter arrayAdapter;

    Location location;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        listView = (ListView) findViewById(R.id.listView);
        listViewContent = new ArrayList<String>();

        listViewContent.add("Finding nearby requests...");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewContent);
        listView.setAdapter(arrayAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            updateLocation(location);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void updateLocation(Location location) {

        final ParseGeoPoint userLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Requests");

        query.whereNear("requesterLocation", userLocation);
        query.setLimit(100);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    Log.i("MyApp", objects.toString());

                    if (objects.size() > 0) {

                        listViewContent.clear();
                        for(ParseObject object : objects) {
                            if(object.get("driverUsername") == null) {
                                //listViewContent.add(String.valueOf(object.get("requesterUsername")));
                                Double distanceInMiles = userLocation.distanceInMilesTo((ParseGeoPoint) object.get("requesterUsername"));
                                listViewContent.add(String.valueOf(String.format("%.1g%n", distanceInMiles)) + " miles");
                            }
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}