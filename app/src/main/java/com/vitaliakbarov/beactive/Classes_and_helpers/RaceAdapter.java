package com.vitaliakbarov.beactive.Classes_and_helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitaliakbarov.beactive.R;

import java.util.ArrayList;

/**
 * Created by vitaliakbarov on 15/03/2017.
 */

public class RaceAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Race> races;

    public RaceAdapter(Context context, ArrayList<Race> races) {
        super(context, R.layout.list_item, races);
        this.context = context;
        this.races = races;
    }

    private class ViewHolder {
        ImageView image;
        TextView date;
        TextView distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);

            viewHolder.image = (ImageView) convertView.findViewById(R.id.list_item_image_view);
            viewHolder.date = (TextView) convertView.findViewById(R.id.list_item_date);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.list_item_distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (races.get(position).getMySportActivity().equals(MyConstants.WALKING_STRING)) {
            //walking img
            viewHolder.image.setImageResource(R.drawable.walking);
        } else if (races.get(position).getMySportActivity().equals(MyConstants.RUNNING_STRING)) {
            //running
            viewHolder.image.setImageResource(R.drawable.running);
        } else {
            // riding
            viewHolder.image.setImageResource(R.drawable.bicycle);
        }
        viewHolder.date.setText(races.get(position).getRaceDate());
        viewHolder.distance.setText((races.get(position).getRaceDistance()));

        return convertView;
    }
}

