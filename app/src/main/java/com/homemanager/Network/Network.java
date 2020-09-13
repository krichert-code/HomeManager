package com.homemanager.Network;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.homemanager.R;
import com.homemanager.ConnectionMessage;
import com.homemanager.Task.Info.InfoObject;

public class Network {

    private View promptView;

    public View createScreen(View view, final AlertDialog dialog, final ConnectionMessage connectionMessage) {
        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        promptView = layoutInflater.inflate(R.layout.network, null);

        SharedPreferences prefs = promptView.getContext().getSharedPreferences(
                "com.homemanager", promptView.getContext().MODE_PRIVATE);

        ((TextView) promptView.findViewById(R.id.netPassword)).setText(
                prefs.getString("com.homemanager.password", "password"));

        ((TextView) promptView.findViewById(R.id.netHost)).setText(
                prefs.getString("com.homemanager.remoteHost", "http://remoteHost.com"));


        Button btnAdd = (Button) promptView.findViewById(R.id.networkCloseButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectionMessage.connectionParametersChanged();
                // btnAdd1 has been clicked
                dialog.dismiss();
            }
        });

        btnAdd = (Button) promptView.findViewById(R.id.networkPassButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //String a = statusMessages.readProperty("password");
                SharedPreferences prefs = promptView.getContext().getSharedPreferences(
                        "com.homemanager", promptView.getContext().MODE_PRIVATE);

                prefs.edit().putString("com.homemanager.password",
                        ((TextView) promptView.findViewById(R.id.netPassword)).getText().toString()).apply();

                //statusMessages.displayHint(R.string.HintSettingsSaveDone);
            }
        });
        return promptView;
    }

}




/*
        btnAdd = (Button) promptView.findViewById(R.id.infoPassButton);
                btnAdd.setOnClickListener(new View.OnClickListener() {
public void onClick(View v) {
        //String a = statusMessages.readProperty("password");
        SharedPreferences prefs = promptView.getContext().getSharedPreferences(
        "com.homemanager", Context.MODE_PRIVATE);

        prefs.edit().putString("com.homemanager.password",
        ((TextView) promptView.findViewById(R.id.editTextTextPassword)).getText().toString()).apply();

        statusMessages.displayHint(R.string.HintSettingsSaveDone);
        }
        });
*/