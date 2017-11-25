package com.vitaliakbarov.beactive.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vitaliakbarov.beactive.Interfaces.OnCompleteListener;
import com.vitaliakbarov.beactive.Classes_and_helpers.MyConstants;
import com.vitaliakbarov.beactive.Classes_and_helpers.Race;
import com.vitaliakbarov.beactive.Classes_and_helpers.SaveFragment;
import com.vitaliakbarov.beactive.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener, View.OnLongClickListener, OnCompleteListener {

    private GoogleMap mMap;
    private LinearLayout timeAndDistanceLayout;
    private LinearLayout fragmentLayout;

    private TextView distance;
    private TextView timerValue;
    private Button start;
    private Button walkingButton;
    private Button runningButton;
    private Button ridingButton;
    private float distanceFromStart = 0;
    private ArrayList<LatLng> points;
    private Polyline line;
    private Location myLocation;
    private LocationManager locationManager;
    private String bestProvider;
    private String mySportActivity = "";
    private LinearLayout activityLayout;
    private boolean drawing = true;
    //    private String finalTime;
//    private String finalDistance;
    private TextView seeMyActivities;
    private long startTime = 5000L;
    private long pusedTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private int onBackPressed = 0;

    // private FragmentManager fm = getSupportFragmentManager();

    private DatabaseReference mDatabase;
    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

    }

    private void init() {
        timerValue = (TextView) findViewById(R.id.timerTV);
        distance = (TextView) findViewById(R.id.distanceTV);
        points = new ArrayList<LatLng>(); //added
        activityLayout = (LinearLayout) findViewById(R.id.my_activity_buttons_layout);
        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(this);
        start.setOnLongClickListener(this);
        walkingButton = (Button) findViewById(R.id.buttonWalk);
        walkingButton.setOnClickListener(this);
        runningButton = (Button) findViewById(R.id.buttonRun);
        runningButton.setOnClickListener(this);
        ridingButton = (Button) findViewById(R.id.buttonBike);
        ridingButton.setOnClickListener(this);
        seeMyActivities = (TextView) findViewById(R.id.see_activities);
        seeMyActivities.setOnClickListener(this);
        timeAndDistanceLayout = (LinearLayout)findViewById(R.id.time_and_distance_layout);
        fragmentLayout = (LinearLayout)findViewById(R.id.fragment_Layout);


        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // init google map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);// closing the icons to google maps app
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        bestProvider = locationManager.getBestProvider(new Criteria(), true);

        GoogleMapOptions options = new GoogleMapOptions();
        options.compassEnabled(true);
        checkPermission();

        mMap.setMyLocationEnabled(true);


        myLocation = locationManager.getLastKnownLocation(bestProvider);
        if(myLocation != null)
        {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
        else
        {
            locationManager.requestLocationUpdates(bestProvider, 3000, 2, this);
        }
        }


    private boolean checkPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(this, "Please enable app PREMISSIONS", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // check for location
    private void checkMyLocation(boolean running) {

        if (running == true) {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            bestProvider = locationManager.getBestProvider(new Criteria(), true);

            checkPermission();

            locationManager.requestLocationUpdates(bestProvider, 5000, 2, this); // 5 sec or 2 meter
            //locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        } else {
            locationManager.removeUpdates(this);
        }
    }

    // locaition changed get new points and save them
    @Override
    public void onLocationChanged(Location location) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        checkPermission();
        location = locationManager.getLastKnownLocation(provider);
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        double latti = location.getLatitude();
        double longi = location.getLongitude();
        LatLng latLng = new LatLng(latti, longi);
        points.add(latLng);
        redrawLine();
    }

    // draw lines between points and count distance
    private void redrawLine() {
        float[] results = new float[1];
        mMap.clear();  //clears all Markers and Polylines
        int i;
        PolylineOptions options = new PolylineOptions().width(35).color(Color.BLUE).geodesic(true);
        for (i = 0; i < points.size(); i++) {

            LatLng point = points.get(i);
            options.add(point);

            if (i == 0) {
                distanceFromStart += 0;
            } else {
                Location.distanceBetween(points.get(i - 1).latitude, points.get(i - 1).longitude, points.get(i).latitude, points.get(i).longitude, results);
            }
        }
        // result[0] has the distance between too points
        if (results[0] < 1.0) {
            results[0] = 0;
        } else {
            if (drawing == false) {
                distanceFromStart += 0;
                drawing = true;
            } else {
                distanceFromStart += results[0];
            }
            int km = (int) distanceFromStart / 1000;
            int meter = (int) distanceFromStart;
            meter = meter % 1000;
            meter = meter / 10;

            distance.setText(km + "." + String.format("%02d", meter) + " km");
            line = mMap.addPolyline(options); //add Polyline
        }
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


    @Override
    public void onClick(View v) {

        if (v == start && start.getText().equals(MyConstants.START)) {
            if (mySportActivity.isEmpty()) {
                Toast.makeText(this, "Please choose your activity for today", Toast.LENGTH_SHORT).show();
                return;
            } else {
                checkMyLocation(true);
                startTime = SystemClock.uptimeMillis() - pusedTime;
                customHandler.postDelayed(updateTimerThread, 0);
                start.setText(MyConstants.PAUSE);
                timeAndDistanceLayout.setVisibility(View.VISIBLE);
                seeMyActivities.setVisibility(View.GONE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        80.0f
                );

                fragmentLayout.setLayoutParams(param);
                return;
            }
        }
        if (v == start && start.getText().equals(MyConstants.PAUSE)) {
            // pause running
            checkMyLocation(false);
            customHandler.removeCallbacks(updateTimerThread);
            start.setText(MyConstants.START);
            drawing = false;

        }
        if (v == walkingButton) {
            mySportActivity = MyConstants.WALKING_STRING;
            activityLayout.setVisibility(View.GONE);
            seeMyActivities.setVisibility(View.GONE);

        }
        if (v == runningButton) {
            mySportActivity = MyConstants.RUNNING_STRING;
            activityLayout.setVisibility(View.GONE);
            seeMyActivities.setVisibility(View.GONE);

        }
        if (v == ridingButton) {
            mySportActivity = MyConstants.RIDING_STRING;
            activityLayout.setVisibility(View.GONE);
            seeMyActivities.setVisibility(View.GONE);

        }
        if (v == seeMyActivities) {
            // go to list of race
            Intent intent = new Intent(this, MyListActivity.class);
            startActivity(intent);
        }
    }

    // starts timer
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            pusedTime = timeInMilliseconds;

            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            mins = mins % 60;

            timerValue.setText("" + hrs + ":" + mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };

    // opens fragment
    @Override
    public boolean onLongClick(View v) {
        if (v == start && start.getText().equals(MyConstants.PAUSE)) {
            checkMyLocation(false);
            customHandler.removeCallbacks(updateTimerThread);
            start.setText(MyConstants.START);
            drawing = false;

            SaveFragment fragment = new SaveFragment();
            fragment.show(getFragmentManager(), "");
        }
        return true;
    }

    @Override
    public void onComplete(boolean result) { // user pressed save the race
        if (result) {
            String date;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
            date = sdf.format(new Date());
            String steps;
            if (mySportActivity.equals(MyConstants.WALKING_STRING)) {
                steps =String.valueOf( (int) ((int) distanceFromStart / 0.75));  // 0.75 avg human step
            } else {
                steps = String.valueOf((int) ((int) distanceFromStart / 1.2));  // 1.2 avg human runing step
            }
            Race race = new Race(timerValue.getText().toString(), distance.getText().toString(), distanceFromStart, timeInMilliseconds, mySportActivity, steps, date);

            //   Toast.makeText(this, "Steps = " + race.getSteps(), Toast.LENGTH_SHORT).show();
            //  Toast.makeText(this, "AvgSpeed = " + race.getAvgSpeed(), Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, "timeInMiliSeconds = " + timeInMilliseconds, Toast.LENGTH_SHORT).show();


            mDatabase.child(androidId).child(date).setValue(race);
        }

    }

    @Override
    public void onBackPressed() {
        onBackPressed++;
        if(onBackPressed == 1 && (activityLayout.getVisibility() == View.VISIBLE)){
            mySportActivity = "";
            seeMyActivities.setVisibility(View.VISIBLE);
            finish();
        }
        else if(onBackPressed == 1&& (activityLayout.getVisibility() == View.GONE)){
            activityLayout.setVisibility(View.VISIBLE);
            mySportActivity = "";
            seeMyActivities.setVisibility(View.VISIBLE);
            onBackPressed = 0;

        }
    }
}
