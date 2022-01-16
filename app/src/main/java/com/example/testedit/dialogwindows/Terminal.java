package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.testedit.Loading;
import com.example.testedit.R;
import com.example.testedit.Work_with_File;

import java.util.concurrent.Executors;

public class Terminal {
    AlertDialog alertDialog;

    String Return = "", urls;
    Context context;
    String str="";
    TextView terminal;
    EditText textsend;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Terminal(Activity context) {
        this.context = context;



        /**скрываем клавитауру при октрыктиии этого дилаогового окна*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = context.getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        final View linearlayout = inflater.inflate(R.layout.terminal, null);
        ImageButton sent=linearlayout.findViewById(R.id.send);
        terminal=linearlayout.findViewById(R.id.terminal);
        textsend =linearlayout.findViewById(R.id.textSend);
        terminal.setVerticalScrollBarEnabled(true);
        terminal.setMovementMethod(new ScrollingMovementMethod());
        terminal.setFocusable(false);/**Отключаем ввод текста*/
        terminal.setCursorVisible(false);/**Отключаем курсор*/
        ratingdialog.setView(linearlayout);
        alertDialog = ratingdialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#151d27")));
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // DialogKeyListener key = new DialogKeyListener();
     //   alertDialog.setOnKeyListener(key);
        ratingdialog.create();
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // str=str+"seteeeeeeeeee steeeeeeeeeeeeee\n";
              //  terminal.setText(str);
               // Start();
                loading.setMessage(textsend.getText().toString());
            }
        });



    }
    public void setSetting(String message){
      //  str=str+setting+"\n";
      //  terminal.setText(str);
        Incoming_Data = message.split(":");
        Start();

    }


/*
    private class DialogKeyListener implements DialogInterface.OnKeyListener {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //   mainInterface.Finishing();
                return false;
            }
            return true;
        }


    }

*/
    public void setTerminal(String message){
          str=str+message+"\n";
          terminal.setText(message);
        /** Метод  с помошью которого можно скорлить пермешать коретку вниз автоматически
         * при заполении данных в окно*/
        final int scrollAmount = terminal.getLayout().getLineTop(terminal.getLineCount()) - terminal.getHeight();
        /** если нет необходимости прокручивать, scrollAmount будет <=0*/
        if (scrollAmount > 0)
            terminal.scrollTo(0, scrollAmount);
        else
            terminal.scrollTo(0, 0);
    }
    String[] Incoming_Data = null;
    Loading loading;
    String Directory = Environment.getExternalStorageDirectory().toString() + "/python/";
    private void Start() {
        //    NewDir = Incoming_Data[0].split("python");
        String[] Connection_Data;
        String info = Work_with_File.readInformation("connect.txt", "", Environment.getExternalStorageDirectory().toString() + "/python/");
        Connection_Data = info.split(":");
        /** Connection_Data[0]=ipadress
         * Connection_Data[1]=port
         * Connection_Data[2]=user
         * Connection_Data[3]=password
         * Connection_Data[4]=usls=exemple=(home/kali/python or home/kali/documents/python
         * Connection_Data[5]=command Python (python3 if pthon>3.0 or if python<3 python
         * */
        urls = Connection_Data[4];

        loading = new Loading((Activity) context,Connection_Data[2], Connection_Data[3], Connection_Data[0], Integer.parseInt(Connection_Data[1]));
        loading.executeOnExecutor(Executors.newScheduledThreadPool(1));
        String[] ProgramName = Incoming_Data[0].replace(Environment.getExternalStorageDirectory().toString() + "/python/", "").split("/");
        String ProgramDirectory = Directory + ProgramName[0];
        String isRun_Directory = Incoming_Data[0].replace(Environment.getExternalStorageDirectory().toString() + "/python/", "") + Incoming_Data[1];
        loading.setCopySend(ProgramDirectory, Connection_Data[4]);
        loading.StartComand(Connection_Data[5] + " " + Connection_Data[4] + isRun_Directory);

    }


}