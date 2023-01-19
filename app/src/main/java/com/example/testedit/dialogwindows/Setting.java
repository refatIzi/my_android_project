package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.WR_File;
import com.example.testedit.connect.Connect;
import com.example.testedit.connect.Protocol;

public class Setting {
    MainInterface mainInterface;
    Context context;

    public Setting(Activity context) {
        this.context = context;
        mainInterface = (MainInterface) context;
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_setting, null);
        ratingdialog.setView(linearlayout);
        final AlertDialog ab = ratingdialog.show();
        /**установка прозрачного фона вашего диалога*/
        ab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button SeveButton = linearlayout.findViewById(R.id.Setting_save);
        final EditText ipAddress = linearlayout.findViewById(R.id.IPaddress);
        final EditText port = linearlayout.findViewById(R.id.port);
        final EditText username = linearlayout.findViewById(R.id.username);
        final EditText password = linearlayout.findViewById(R.id.password);
        final EditText projectDirHost = linearlayout.findViewById(R.id.urls);
        final EditText Python = linearlayout.findViewById(R.id.pythver);
        final SeekBar TextSizeBar = linearlayout.findViewById(R.id.TextSizeBar);
        final TextView textSize = linearlayout.findViewById(R.id.textSize);
        final TextView Information = linearlayout.findViewById(R.id.Information);

        TextSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSize.setText("Text size = " + progress);
                mainInterface.setNumberCode(progress);
                mainInterface.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String[] separated;
        String info = WR_File.readInformation("connect.txt", "", Environment.getExternalStorageDirectory().toString() + "/python/");
        separated = info.split(":");
        if (separated.length > 0) {
            ipAddress.setText(separated[0]);
        }
        if (separated.length > 1) {
            port.setText(separated[1]);
        }
        if (separated.length > 2) {
            username.setText(separated[2]);
        }
        if (separated.length > 3) {
            password.setText(separated[3]);
        }
        if (separated.length > 4) {
            projectDirHost.setText(separated[4]);
        }
        if (separated.length > 5) {
            Python.setText(separated[5]);
        }
        if (separated.length > 6) {
            TextSizeBar.setProgress(Integer.parseInt(separated[6]));
        }

        if (TextSizeBar.getProgress() == 0) {
            mainInterface.setNumberCode(20);
            mainInterface.setTextSize(20);
        }
        SeveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect connect = Connect.newHost()
                        .ipAddress(ipAddress.getText().toString())
                        .setUserName(username.getText().toString())
                        .setPassword(password.getText().toString())
                        .setPort(port.getText().toString())
                        .setProtocol(Protocol.SSH)
                        .setProjectDirHost(projectDirHost.getText().toString())
                        .accept();

                Toast.makeText(context,"Host "+connect,Toast.LENGTH_LONG).show();
                WR_File.saveFile("connect.txt", ipAddress.getText().toString() + ":" + port.getText().toString()
                        + ":" + username.getText().toString() + ":" + password.getText().toString()
                        + ":" + projectDirHost.getText().toString() + ":" + Python.getText().toString() + ":" + TextSizeBar.getProgress(), Environment.getExternalStorageDirectory().toString() + "/python/");
                ab.cancel();
            }
        });
        ratingdialog.create();
    }
}
