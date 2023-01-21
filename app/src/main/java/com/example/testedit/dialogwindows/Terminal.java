package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
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
import com.example.testedit.setting.Data;

import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Terminal {
    AlertDialog alertDialog;
    String[] Incoming_Data = null;
    Loading loading;
    String Directory = Environment.getExternalStorageDirectory().toString() + "/python/";
    Context context;
    TextView terminal;
    EditText textSend;

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
        ImageButton sent = linearlayout.findViewById(R.id.send);
        terminal = linearlayout.findViewById(R.id.terminal);
        textSend = linearlayout.findViewById(R.id.textSend);
        terminal.setVerticalScrollBarEnabled(true);
        terminal.setMovementMethod(new ScrollingMovementMethod());
        terminal.setFocusable(false);/**Отключаем ввод текста*/
        terminal.setCursorVisible(false);/**Отключаем курсор*/
        terminal.addTextChangedListener(new TextWatcher() {
            ColorScheme numbers = new ColorScheme(
                    Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)"),
                    Color.parseColor("#2f5f93")
            );
            ColorScheme error = new ColorScheme(
                    Pattern.compile(
                            "\\b(Traceback|NameError|line|module|SyntaxError)\\b"),
                    Color.parseColor("#3e9cca")
            );
            ColorScheme errorName = new ColorScheme(
                    Pattern.compile(
                            "\\b(name|File|python|python3)\\b"),
                    Color.parseColor("#fb4f4f")
            );
            ColorScheme World = new ColorScheme(
                    Pattern.compile(
                            "\\b(Omega|ifconfig|ls|ls -al|cd|pwd|mkdir|rm|rm -r|cp -r|cat >|more|head|tail|)\\b"),
                    Color.parseColor("#2f5f93")
            );

            ColorScheme Name = new ColorScheme(
                    Pattern.compile(
                            "\\b(root|invalid|syntax)\\b"),
                    Color.parseColor("#ffc82d")
            );
            ColorScheme kovichki = new ColorScheme(
                    Pattern.compile("\"([^\"]+)\""),
                    Color.parseColor("#b4794c")
            );

            final ColorScheme[] schemes = {error, errorName, Name, kovichki, World, numbers};

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                removeSpans(s, ForegroundColorSpan.class);
                for (ColorScheme scheme : schemes) {
                    for (Matcher m = scheme.pattern.matcher(s); m.find(); ) {
                        s.setSpan(new ForegroundColorSpan(scheme.color),
                                m.start(),
                                m.end(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }
                }

            }

            void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
                CharacterStyle[] spans = e.getSpans(0, e.length(), type);
                for (CharacterStyle span : spans) {
                    e.removeSpan(span);
                }
            }

            class ColorScheme {
                final Pattern pattern;
                final int color;

                ColorScheme(Pattern pattern, int color) {
                    this.pattern = pattern;
                    this.color = color;
                }
            }
        });
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
                loading.setMessage(textSend.getText().toString());
                textSend.getText().clear();
            }
        });


    }

    public void setSetting(String message) {
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
    public void setTerminal(String message) {
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


    private void Start() {
        String[] Connection_Data;
        String info = new Data().readFile( Environment.getExternalStorageDirectory().toString() + "/python/connect.txt");
        Connection_Data = info.split(":");
        /** Connection_Data[0]=ipadress
         * Connection_Data[1]=port
         * Connection_Data[2]=user
         * Connection_Data[3]=password
         * Connection_Data[4]=usls=exemple=(home/kali/python or home/kali/documents/python
         * Connection_Data[5]=command Python (python3 if pthon>3.0 or if python<3 python
         * */
        loading = new Loading((Activity) context, Connection_Data[2], Connection_Data[3], Connection_Data[0], Integer.parseInt(Connection_Data[1]));
        loading.executeOnExecutor(Executors.newScheduledThreadPool(1));
        String[] ProgramName = Incoming_Data[0].replace(Environment.getExternalStorageDirectory().toString() + "/python/", "").split("/");
        String ProgramDirectory = Directory + ProgramName[0];
        String isRun_Directory = Incoming_Data[0].replace(Environment.getExternalStorageDirectory().toString() + "/python/", "") + Incoming_Data[1];
        loading.setCopySend(ProgramDirectory, Connection_Data[4]);
        // terminal.setText(Connection_Data[5] + " " + Connection_Data[4] + isRun_Directory+" "+Incoming_Data[3]);

        try {
            loading.StartComand(Connection_Data[5] + " " + Connection_Data[4] + isRun_Directory + " " + Incoming_Data[3]);

        } catch (Exception e) {
        }


    }


}