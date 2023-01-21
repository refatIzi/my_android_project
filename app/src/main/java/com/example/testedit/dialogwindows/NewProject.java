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
import android.widget.Toast;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.setting.DataSetting;

import java.io.File;

public class NewProject {
    String Directory;
    MainInterface mainInterface;
    Context context;
    /**
     * Метод сохранение фаила
     */
    public NewProject(Activity context) {
        this.context=context;
        mainInterface=(MainInterface) context;
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_save, null);
        ratingdialog.setView(linearlayout);
        final AlertDialog ab = ratingdialog.show();
        /**установка прозрачного фона вашего диалога*/
        ab.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText SaveEdit = linearlayout.findViewById(R.id.saveEdit);
        final EditText Project = linearlayout.findViewById(R.id.Project);
        ImageButton buttonsend = linearlayout.findViewById(R.id.buttonsave);
        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Project.getText().length() > 0) {
                    Directory = Environment.getExternalStorageDirectory().toString() + "/python/";
                    Directory = Directory + Project.getText() + "_project/";
                    /**Create new Directory */
                    File theDir = new File(Directory);
                    if (!theDir.exists()) {
                        theDir.mkdirs();
                    }
                    //      Toast.makeText(MainActivity.this, "" + directory, Toast.LENGTH_LONG).show();
                } else {
                }

                /*if (editText.getText().length() == 0) {
                    editText.setText("print(\"Hello FEBO\")");
                    Work_with_File.saveFile(SaveEdit.getText().toString() + ".py", editText.getText().toString(), Directory);
                } else {
                    Work_with_File.saveFile(SaveEdit.getText().toString() + ".py", editText.getText().toString(), Directory);
                }
                 */
              //  editText.clearFocus();
                mainInterface.setEditText("print(\"Hello FEBO\")");
                DataSetting.saveFile(SaveEdit.getText().toString() + ".py", "print(\"Hello FEBO\")", Directory);
                mainInterface.setFileName(SaveEdit.getText().toString() + ".py");
                mainInterface.setDirectory(Directory);
                ab.cancel();
                Toast.makeText(context, "" + Directory, Toast.LENGTH_SHORT).show();
            }
        });
        ratingdialog.create();

    }
}
