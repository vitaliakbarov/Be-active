package com.vitaliakbarov.beactive.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.vitaliakbarov.beactive.Classes_and_helpers.DownloadThread;
import com.vitaliakbarov.beactive.Interfaces.OnDownloadCompleteListener;
import com.vitaliakbarov.beactive.R;
import com.vitaliakbarov.beactive.Classes_and_helpers.Race;
import com.vitaliakbarov.beactive.Classes_and_helpers.RaceAdapter;
import com.google.gson.Gson;
import java.util.ArrayList;
import static com.vitaliakbarov.beactive.Classes_and_helpers.MyConstants.RACE_STRING;


public class MyListActivity extends Activity implements AdapterView.OnItemClickListener, OnDownloadCompleteListener {

    private ListView listView;
    private RaceAdapter adapter;
    private String androidId;
    private Race race;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        // get android unic id
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // download data from firebase
        DownloadThread thread = new DownloadThread(this, this, androidId);
        thread.start();
    }


    //item on list was clicked, parse object to json and send to DetailsActivity
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        race = new Race();
        race = (Race) parent.getItemAtPosition(position);
        Gson gson = new Gson();
        String json = gson.toJson(race);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(RACE_STRING, json);
        startActivity(intent);
    }


    @Override
    public void displayList(ArrayList<Race> races) {
        adapter = new RaceAdapter(this, races);
        listView.setAdapter(adapter);
    }
}
