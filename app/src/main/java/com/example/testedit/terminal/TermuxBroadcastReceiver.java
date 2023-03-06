package com.example.testedit.terminal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TermuxBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Toast.makeText(context, "Received message: " + message, Toast.LENGTH_SHORT).show();
        // Do something with the message, e.g., execute a command in Termux
    }


}