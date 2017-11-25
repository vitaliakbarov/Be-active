package com.vitaliakbarov.beactive.Classes_and_helpers;

/**
 * Created by vitaliakbarov on 22/10/2017.
 */

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.vitaliakbarov.beactive.Interfaces.OnDownloadCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by vitaliakbarov on 15/03/2017.
 */

public class DownloadThread extends Thread {

    private String androidId;
    private DatabaseReference mDatabase;
    private OnDownloadCompleteListener listener;
    private Handler handler;
    private ArrayList<Race> races = new ArrayList<>();


    public DownloadThread(OnDownloadCompleteListener listener, Context context, String androidId) {
        this.androidId = androidId;
        this.listener = listener;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        handler = new Handler();

    }

    // get data from firebase
    @Override
    public void run() {

        Query myQuery = mDatabase.child(androidId);
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    races.add(new Race(userSnap));
                    Log.d("Race" ,userSnap.toString());
                }
                Collections.reverse(races);

                // download complete, calls to show the list
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.displayList(races);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public ArrayList<Race> getRaces() {
        return races;
    }
}
