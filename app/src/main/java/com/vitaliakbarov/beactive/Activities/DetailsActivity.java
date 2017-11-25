package com.vitaliakbarov.beactive.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vitaliakbarov.beactive.Classes_and_helpers.MyConstants;
import com.vitaliakbarov.beactive.Classes_and_helpers.Race;
import com.vitaliakbarov.beactive.R;

public class DetailsActivity extends Activity {

    private ImageView image;
    private TextView date;
    private TextView myActivity;
    private TextView distance;
    private TextView time;
    private TextView avgSpeed;
    private TextView steps;
    private TextView stepsTextView;
    private Race race;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        race = new Race();
        Bundle bundle = getIntent().getExtras();
        String objAsJson = bundle.getString(MyConstants.RACE_STRING);
        Gson gson = new Gson();
        race = gson.fromJson(objAsJson, Race.class);

        init();
    }

    private void init() {

        image = (ImageView) findViewById(R.id.details_activity_image_view);
        date = (TextView) findViewById(R.id.details_activity_date);
        myActivity = (TextView) findViewById(R.id.details_activity_activity);
        distance = (TextView) findViewById(R.id.details_activity_distance);
        time = (TextView) findViewById(R.id.details_activity_time);
        avgSpeed = (TextView) findViewById(R.id.details_activity_avg_speed);
        steps = (TextView) findViewById(R.id.details_activity_steps);
        stepsTextView = (TextView)findViewById(R.id.steps_text_view);

        if (race.getMySportActivity().equals(MyConstants.WALKING_STRING)) {
            image.setImageResource(R.drawable.walking);
        } else if (race.getMySportActivity().equals(MyConstants.RUNNING_STRING)) {
            image.setImageResource(R.drawable.running);
        } else {
            image.setImageResource(R.drawable.bicycle);
        }
        date.setText(race.getRaceDate());
        myActivity.setText(race.getMySportActivity());
        distance.setText(race.getRaceDistance());
        time.setText(race.getRaceTime());
        avgSpeed.setText(race.getAvgSpeed() + " Km/H");
        if(race.getMySportActivity().equals("riding")){
            Log.d("Steps", "riding");
            steps.setVisibility(View.INVISIBLE);
            stepsTextView.setVisibility(View.INVISIBLE);
        }else {
            steps.setText((race.getSteps()));
        }
    }

}
