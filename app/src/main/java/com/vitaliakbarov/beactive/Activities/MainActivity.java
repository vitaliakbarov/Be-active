package com.vitaliakbarov.beactive.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.vitaliakbarov.beactive.Classes_and_helpers.AbsRuntimePermission;
import com.vitaliakbarov.beactive.Classes_and_helpers.MyConstants;
import com.vitaliakbarov.beactive.R;

public class MainActivity extends AbsRuntimePermission {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //request premissions

        requestAppPermissions(new String[]{

                        android.Manifest.permission.ACCESS_FINE_LOCATION},
                R.string.msg, MyConstants.REQUEST_PERMISSION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        //granted
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}