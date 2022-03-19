package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.testedit.MainInterface;
import com.example.testedit.R;

public class Information {
    MainInterface mainInterface;

    public  Information(Activity context,String txt) {
        mainInterface=(MainInterface) context;
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_information, null);
        ratingdialog.setView(linearlayout);
        final AlertDialog ab = ratingdialog.show();
        /**установка прозрачного фона вашего диалога*/
        ab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView text = linearlayout.findViewById(R.id.text);
        text.setText(txt+ " this is will be information about words  ");
        ratingdialog.create();
    }
}
