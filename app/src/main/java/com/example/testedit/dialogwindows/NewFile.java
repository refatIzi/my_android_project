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

public class NewFile {
    String directory;
    MainInterface mainInterface;
    Context context;
    public NewFile(Activity context,String directory) {
        this.context=context;
        mainInterface=(MainInterface) context;
        this.directory =directory;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_save_file, null);
        dialog.setView(linearlayout);
        final AlertDialog alertDialog = dialog.show();
        /**установка прозрачного фона вашего диалога*/
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText name = linearlayout.findViewById(R.id.saveEdit_file);
        ImageButton buttonSend_file = linearlayout.findViewById(R.id.buttonsave_file);
        buttonSend_file.setOnClickListener(v -> {
            String fileName = name.getText().toString() + ".py";
            String code = "print(\"Hello FEBO\")";
            mainInterface.setEditText(code);
            new Data().createFile(fileName, code, directory);
            mainInterface.setFileName(fileName);
            mainInterface.setDirectory(directory);
            alertDialog.cancel();
            Toast.makeText(context, "" + directory, Toast.LENGTH_SHORT).show();
        });

    }
}
