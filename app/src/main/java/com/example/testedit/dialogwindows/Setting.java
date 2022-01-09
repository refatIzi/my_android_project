package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.Work_with_File;

public class Setting {
    MainInterface mainInterface;
    Context context;

    public  Setting(Activity context) {
        this.context = context;
        mainInterface=(MainInterface) context;
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_setting, null);
        ratingdialog.setView(linearlayout);
        final AlertDialog ab = ratingdialog.show();
        /**установка прозрачного фона вашего диалога*/
        ab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton SeveButton = linearlayout.findViewById(R.id.Setting_save);
        final EditText IpAddress = linearlayout.findViewById(R.id.IPaddress);
        final EditText Port = linearlayout.findViewById(R.id.port);
        final EditText Username = linearlayout.findViewById(R.id.username);
        final EditText Password = linearlayout.findViewById(R.id.password);
        final EditText Urls = linearlayout.findViewById(R.id.urls);
        final EditText Python = linearlayout.findViewById(R.id.pythver);
        final SeekBar TextSizeBar = linearlayout.findViewById(R.id.TextSizeBar);
        final TextView textSize = linearlayout.findViewById(R.id.textSize);
        TextView Information = linearlayout.findViewById(R.id.Information);

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
        String info = Work_with_File.readInformation("connect.txt", "", Environment.getExternalStorageDirectory().toString() + "/python/");
        separated = info.split(":");
        if (separated.length > 0) {
            IpAddress.setText(separated[0]);
        }
        if (separated.length > 1) {
            Port.setText(separated[1]);
        }
        if (separated.length > 2) {
            Username.setText(separated[2]);
        }
        if (separated.length > 3) {
            Password.setText(separated[3]);
        }
        if (separated.length > 4) {
            Urls.setText(separated[4]);
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
                Work_with_File.saveFile("connect.txt", IpAddress.getText().toString() + ":" + Port.getText().toString()
                        + ":" + Username.getText().toString() + ":" + Password.getText().toString()
                        + ":" + Urls.getText().toString() + ":" + Python.getText().toString() + ":" + TextSizeBar.getProgress(), Environment.getExternalStorageDirectory().toString() + "/python/");
                ab.cancel();
            }
        });
        ratingdialog.create();
    }
}
