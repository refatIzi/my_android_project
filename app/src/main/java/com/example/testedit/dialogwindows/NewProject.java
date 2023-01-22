package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.setting.Data;

public class NewProject {
    String directory;
    MainInterface mainInterface;
    Context context;
    String ONION_DIR = new Data().FEB_ONION_DIR;


    public NewProject(Activity context) {
        this.context = context;
        mainInterface = (MainInterface) context;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_save, null);
        dialog.setView(linearlayout);
        final AlertDialog alertDialog = dialog.show();
        /**установка прозрачного фона вашего диалога*/
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText saveEdit = linearlayout.findViewById(R.id.saveEdit);
        final EditText project = linearlayout.findViewById(R.id.Project);
        ImageButton buttonsend = linearlayout.findViewById(R.id.buttonsave);
        buttonsend.setOnClickListener(v -> {
            String fileName = saveEdit.getText().toString() + ".py";
            String code = "print(\"Hello FEBO\")";
            if (project.getText().length() > 0) {
                directory = ONION_DIR;
                directory = directory + project.getText() + "_project/";
                new Data().checkDirectory(directory);
            } else {
            }

            mainInterface.setEditText(code);
            new Data().createFile(fileName, code, directory);
            mainInterface.setFileName(fileName);
            mainInterface.setDIRECTORY(directory);
            alertDialog.cancel();
            Toast.makeText(context, "" + directory, Toast.LENGTH_SHORT).show();
        });
        dialog.create();

    }
}
