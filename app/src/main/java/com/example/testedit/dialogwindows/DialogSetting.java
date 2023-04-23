package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.connect.Connect;
import com.example.testedit.connect.Protocol;
import com.example.testedit.date.FData;
import com.example.testedit.date.DataSetting;
import com.example.testedit.setting.Setting;

public class DialogSetting {
    MainInterface mainInterface;
    Context context;
    DataSetting dataSaving;

    public DialogSetting(Activity context) {
        this.context = context;
        mainInterface = (MainInterface) context;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_setting, null);
        dialog.setView(linearlayout);
        final AlertDialog alertDialog = dialog.show();
        /**установка прозрачного фона вашего диалога*/
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button seveButton = linearlayout.findViewById(R.id.Setting_save);
        final EditText ipAddress = linearlayout.findViewById(R.id.IPaddress);
        final EditText port = linearlayout.findViewById(R.id.port);
        final EditText username = linearlayout.findViewById(R.id.username);
        final EditText password = linearlayout.findViewById(R.id.password);
        final EditText projectDirHost = linearlayout.findViewById(R.id.urls);
        final EditText python = linearlayout.findViewById(R.id.pythver);
        final SeekBar textSizeBar = linearlayout.findViewById(R.id.TextSizeBar);
        final TextView textSize = linearlayout.findViewById(R.id.textSize);
        final TextView Information = linearlayout.findViewById(R.id.Information);
        dataSaving = new FData().readData();

        textSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        try {
                ipAddress.setText(dataSaving.getConnect().getIpAddress());
                port.setText(dataSaving.getConnect().getPort());
                username.setText(dataSaving.getConnect().getUserName());
                password.setText(dataSaving.getConnect().getPassword());
                projectDirHost.setText(dataSaving.getConnect().getProjectDirHost());
                python.setText(dataSaving.getConnect().getPython().toString());
                textSizeBar.setProgress(dataSaving.getSetting().getTextSize());
            if (textSizeBar.getProgress() == 0) {
                mainInterface.setNumberCode(16);
                mainInterface.setTextSize(16);
            }

        } catch (Exception e) {

        }

        seveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect connect = Connect
                        .newHost()
                        .ipAddress(ipAddress.getText().toString())
                        .setUserName(username.getText().toString())
                        .setPassword(password.getText().toString())
                        .setPort(port.getText().toString())
                        .setProtocol(Protocol.SSH)
                        .setProjectDirHost(projectDirHost.getText().toString())
                        .setPython(Connect.Python.python3)
                        .accept();
                Setting setting = Setting
                        .newSet()
                        .setTextSize(textSizeBar.getProgress())
                        .accept();
                dataSaving = DataSetting
                        .newSetting()
                        .setConnect(connect)
                        .setSetting(setting)
                        .accept();
                new FData().writeData(dataSaving);
                       alertDialog.cancel();
            }
        });
        dialog.create();
    }
}
