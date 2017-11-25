package com.vitaliakbarov.beactive.Classes_and_helpers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;



    public class Race {

        private String raceTime;
        private String raceDistance;
        private String raceDate;
        private String mySportActivity;
        private String avgSpeed = "0";
        private String  steps;
        private double distance;
        private long timeInmiliSecond;

        public Race(String raceTime, String raceDistance, double distance, long timeInmiliSecond, String mySportActivity, String  steps, String raceDate) {

            this.raceTime = raceTime;
            this.raceDistance = raceDistance;
            this.distance = distance;
            this.timeInmiliSecond = timeInmiliSecond;
            this.mySportActivity = mySportActivity;
            this.steps = steps;
            this.raceDate = raceDate;


        }

        public Race() { // for firebase

        }

        public Race(DataSnapshot snapshot) {  // firebase object

            avgSpeed = (String) snapshot.child("avgSpeed").getValue();
            mySportActivity = (String) snapshot.child("mySportActivity").getValue();
            raceDate = (String) snapshot.child("raceDate").getValue();
            raceDistance = (String) snapshot.child("raceDistance").getValue();
            raceTime = (String) snapshot.child("raceTime").getValue();
            steps = (String ) snapshot.child("steps").getValue();
            Log.d("Race", snapshot.toString());
        }

        public String  getSteps() {
            return steps;
        }

        public String getAvgSpeed() {
            if (distance == 0) {
                return avgSpeed;
            }

            double seconds = (double) (timeInmiliSecond / 1000); // miliSeconds to seconds
            double distance = this.distance;
            double avg;
            avg = distance / seconds;  // avg  meter/second
            avg = (avg * 3.6); // avg kilometer/hour

            avgSpeed = String.valueOf(avg);
            avgSpeed = String.format("%.2f", avg);
            return avgSpeed;
        }

        // getters and setters
        public String getRaceTime() {
            return raceTime;
        }

        public String getRaceDistance() {
            return raceDistance;
        }

        public String getMySportActivity() {
            return mySportActivity;
        }

        public String getRaceDate() {
            return raceDate;
        }

        public void setRaceTime(String raceTime) {
            this.raceTime = raceTime;
        }

        public void setRaceDistance(String raceDistance) {
            this.raceDistance = raceDistance;
        }

        public void setRaceDate(String raceDate) {
            this.raceDate = raceDate;
        }

        public void setMySportActivity(String mySportActivity) {
            this.mySportActivity = mySportActivity;
        }

        public void setSteps(String  steps) {
            this.steps = steps;
        }

        public void setAvgSpeed(String avgSpeed) {
            this.avgSpeed = avgSpeed;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public void setTimeInmiliSecond(long timeInmiliSecond) {
            this.timeInmiliSecond = timeInmiliSecond;
        }


    }


