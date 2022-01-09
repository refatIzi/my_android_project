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
import com.example.testedit.Work_with_File;

public class NewFile {
    String Directory;
    MainInterface mainInterface;
    Context context;
    public NewFile(Activity context,String Directory) {
        this.context=context;
        mainInterface=(MainInterface) context;
        this.Directory=Directory;
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_save_file, null);
        ratingdialog.setView(linearlayout);
        final AlertDialog ab = ratingdialog.show();
        /**установка прозрачного фона вашего диалога*/
        ab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText FileName_ = linearlayout.findViewById(R.id.saveEdit_file);
        ImageButton buttonSend_file = linearlayout.findViewById(R.id.buttonsave_file);
        buttonSend_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainInterface.setEditText("print(\"Hello FEBO\")");
                Work_with_File.saveFile(FileName_.getText().toString() + ".py", "print(\"Hello FEBO\")", Directory);
                mainInterface.setFileName(FileName_.getText().toString() + ".py");
                mainInterface.setDirectory(Directory);
                ab.cancel();
                Toast.makeText(context, "" + Directory, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
