package com.vitaliakbarov.beactive.Classes_and_helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.vitaliakbarov.beactive.R;
import com.vitaliakbarov.beactive.Activities.MapsActivity;
import com.vitaliakbarov.beactive.Interfaces.OnCompleteListener;

/**
 * Created by vitaliakbarov on 14/03/2017.
 */

//create and show dialog fragment
public class SaveFragment extends DialogFragment {

    private Context context;
    private boolean save;
    private OnCompleteListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_save, null);

        Button buttonSave = (Button) view.findViewById(R.id.saveBtn);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save = true;
                listener.onComplete(save);
                dismiss();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        Button buttonCancel = (Button) view.findViewById(R.id.cancelBtn);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.listener = (OnCompleteListener) context;
    }

    public boolean isSave() {
        return save;
    }
}

