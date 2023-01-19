package com.example.testedit;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Download extends AppCompatActivity implements View.OnClickListener {
//https://icon-icons.com/ru/pack/Papirus-Apps/1381


    String Return = "", urls;
    ImageButton send;

    static EditText textsend;
    static TextView txt;

    String store = null;
    String[] Incoming_Data = null;
    Loading loading;
    String Directory = Environment.getExternalStorageDirectory().toString() + "/python/";
    int cnt;
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        send = findViewById(R.id.send);
        textsend = findViewById(R.id.textSend);
        txt = findViewById(R.id.txt);




        send.setOnClickListener(this);
        /**Тут мы вычилсяем высоту и ширину экрана потом отнимаем от высоты 53dp переведенное в пиксели
         * и задаем ноовую высоту для ТХТ так как если не определенный размер для ТХТ то он не рабоатет
         * я не знаю как с этим бороться и нашол такое решение как задать размер при старте Активити*/


        txt.setVerticalScrollBarEnabled(true);
        txt.setMovementMethod(new ScrollingMovementMethod());
        txt.setFocusable(false);/**Отключаем ввод текста*/
        txt.setCursorVisible(false);/**Отключаем курсор*/
        txt.addTextChangedListener(new TextWatcher() {
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

            final ColorScheme[] schemes = {error, errorName, Name, kovichki, World};

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
        txt.clearFocus();


        Bundle extras = getIntent().getExtras();
        /** Парсим сообшение которое пришло с идентификатором ( : )
         * делим на части первая часть путь где лижит сам файл
         * вторая часть это название файла
         * Incoming_Data[0]=the path to the file путь к фаилу
         * Incoming_Data[1]= название фаила
         * Incoming_Data[2]= сообшение о том что аил находиться в папке проекта или нет*/
        if (extras != null) {
            store = extras.getString("downaload");/**download это идентификатор*/
            Incoming_Data = store.split(":");
        }
        setTitle("Loading " + Incoming_Data[1]);


        Start();
    }
    public void SetResult(String Return)
    {
        this.Return=Return;
    }
   // String[] NewDir;
    private void Start() {
    //    NewDir = Incoming_Data[0].split("python");
        String[] Connection_Data;
        String info = WR_File.readInformation("connect.txt", "", Environment.getExternalStorageDirectory().toString() + "/python/");
        Connection_Data = info.split(":");
        /** Connection_Data[0]=ipadress
         * Connection_Data[1]=port
         * Connection_Data[2]=user
         * Connection_Data[3]=password
         * Connection_Data[4]=usls=exemple=(home/kali/python or home/kali/documents/python
         * Connection_Data[5]=command Python (python3 if pthon>3.0 or if python<3 python
         * */
        urls = Connection_Data[4];

        loading = new Loading(Download.this,Connection_Data[2], Connection_Data[3], Connection_Data[0], Integer.parseInt(Connection_Data[1]));
        loading.executeOnExecutor(Executors.newScheduledThreadPool(1));
        String[] ProgramName = Incoming_Data[0].replace(Environment.getExternalStorageDirectory().toString() + "/python/", "").split("/");
        String ProgramDirectory = Directory + ProgramName[0];
        String isRun_Directory = Incoming_Data[0].replace(Environment.getExternalStorageDirectory().toString() + "/python/", "") + Incoming_Data[1];
        loading.setCopySend(ProgramDirectory, Connection_Data[4]);
        loading.StartComand(Connection_Data[5] + " " + Connection_Data[4] + isRun_Directory);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                loading.setMessage(textsend.getText().toString());
                break;

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.off:

                try {
                    loading.cancel(true);
                    loading.StopConnect();
                    SendMessage();
                 //   finish();
                } catch (Exception e) {

                }

                return true;
            case R.id.restart:

                try {
                    Start();
                } catch (Exception e) {

                }

                return true;
            case R.id.terminal:
                /**
                 * Метот с помошью котрого мы сохроняем команду в буфер для того чтобы
                 * мы могли его не писать руками а вставить команду в терминал из буфера
                 * в другой терминал*/
                /**
                 * ссылка про метод копирования
                 * https://www.fandroid.info/android-studio-tutorial-clipboard-rabota-s-buferom-obmena/
                 * КОпируем команду в буфер и открываем SSH клиент и вставляем команду из буфера чтобы самим не писать
                 */
                String[] sepread;
                String info = WR_File.readInformation("connect.txt", "", Environment.getExternalStorageDirectory().toString() + "/python/");
                sepread = info.split(":");
                Uri location = null;
                try {
                    /** sepread[0]=ipadress
                     * sepread[1]=port
                     * sepread[2]=user
                     * sepread[3]=password
                     * sepread[4]=usls=exemple=(home/kali/python or home/kali/documents/python
                     * sepread[5]=command Python (python3 if pthon>3.0 or if python<3 python
                     * */
                    //session = jsch.getSession(sepread[2], sepread[0], Integer.parseInt(sepread[1]));
                    location = Uri.parse("ssh://" + sepread[2] + "@" + sepread[0] + ":" + sepread[1] + "/#Python");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipData = ClipData.newPlainText("", sepread[5] + " /" + sepread[4] + "/" + Incoming_Data[1]);
                clipboardManager.setPrimaryClip(clipData);


                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(mapIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void sendText(String message) {
        txt.setText(message);
        /** Метод  с помошью которого можно скорлить пермешать коретку вниз автоматически
         * при заполении данных в окно*/
        final int scrollAmount = txt.getLayout().getLineTop(txt.getLineCount()) - txt.getHeight();
        /** если нет необходимости прокручивать, scrollAmount будет <=0*/
        if (scrollAmount > 0)
            txt.scrollTo(0, scrollAmount);
        else
            txt.scrollTo(0, 0);
    }

    public static void ClearText() {
        textsend.getText().clear();
    }


    public void SendMessage() {
        /**
         * отправка обратна данных
         */
        Intent intent = new Intent(Download.this,MainActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, loading.getResult());
        setResult(RESULT_OK, intent);



    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", cnt);
     //   Log.d(LOG_TAG, "onSaveInstanceState");
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cnt = savedInstanceState.getInt("count");
      //  Log.d(LOG_TAG, "onRestoreInstanceState");
    }
}
