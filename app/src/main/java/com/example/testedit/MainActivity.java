package com.example.testedit;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testedit.dialogwindows.DialogSetting;
import com.example.testedit.dialogwindows.Information;
import com.example.testedit.dialogwindows.NewFile;
import com.example.testedit.dialogwindows.NewProject;
import com.example.testedit.dialogwindows.Open;
import com.example.testedit.dialogwindows.Terminal;
import com.example.testedit.helpinfo.Help;
import com.example.testedit.permission.Permission;
import com.example.testedit.setting.Data;
import com.example.testedit.visualization.Visualization;
import com.example.testedit.visualization.Watch;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements MainInterface {

    EditText editText;
    TextView numberCode;
    String input;
    String fileName;
    private String directory;
    String project_Name = "no project";
    Help help;
    FragmentTransaction fragment;
    String ONION_DIR = new Data().FEB_ONION_DIR;


    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Permission();

        /**Запрет на поворот экрана*/
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /**разрешения  которые я тут исползовал*/

        new Watch(this).execute();
        numberCode = findViewById(R.id.numberCode);
        editText = findViewById(R.id.txtCode);
        fragment = getFragmentManager().beginTransaction();
        help = new Help(MainActivity.this);
        fragment.replace(R.id.liner, help);
        fragment.commit();
        directory = ONION_DIR;
        editText.setTextSize(new Data().setting().getTextSize());
        numberCode.setTextSize(new Data().setting().getTextSize());
        new Data().checkDirectory(ONION_DIR);
        new Open(MainActivity.this, directory);

        editText.addTextChangedListener(new TextWatcher() {

            //до изменении текста
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            /**при изменении текста и добавлениии текста и переходе на новую строку*/
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numberOfConstruction(0);
            }

            private boolean isReached = false;
            // после изменении текста
            @Override
            public void afterTextChanged(Editable s) {
                removeSpans(s, ForegroundColorSpan.class);
                for (Visualization.TextColor tetxtColor : Visualization.getColors()) {
                    for (Matcher m = tetxtColor.pattern.matcher(s); m.find(); ) {
                        s.setSpan(new ForegroundColorSpan(tetxtColor.color),
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


        });

        /**следим за вводом текста
         * Метод для подсказки */

        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            // Accept only letter & digits ; otherwise just return
            help.clear();
            if (source.length() > 0) {
                //    for (int i = 0; i < text.length; i++) {
                //         if (text[i].startsWith(source.toString()))
                //   help.helpAdd(text[i] );
                help.helpAdd(source.toString());
                //   Toast.makeText(MainActivity.this, text[i] + " ~ " + source.toString(), Toast.LENGTH_SHORT).show();
                //    }
            }
            //    if(source.toString().equals("if"))
            //    Toast.makeText(MainActivity.this,""+source.toString().length(),Toast.LENGTH_SHORT).show();
            return null;
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * Метод котрый выводит номурецию строки
     */
    public void numberOfConstruction(int errLine) {
        int line = editText.getLineCount();
        String number = "";
        try {
            for (int i = 1; i <= line; i++) {
                number = number + i + "\n";
            }
            numberCode.clearFocus();
            int error;
            if (errLine == 0) {
                numberCode.setText(number);
            } else if (errLine < 10) {
                error = 2 * (errLine - 1);
                final SpannableStringBuilder text = new SpannableStringBuilder(number);
                final ForegroundColorSpan style = new ForegroundColorSpan(Color.rgb(255, 0, 0));
                text.setSpan(style, error, error + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                numberCode.setText(text);
            } else if (errLine >= 10) {
                error = 3 * (errLine - 9) + 15;
                final SpannableStringBuilder text = new SpannableStringBuilder(number);
                final ForegroundColorSpan style = new ForegroundColorSpan(Color.rgb(255, 0, 0));
                text.setSpan(style, error, error + 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                numberCode.setText(text);
            } else {
            }
        } catch (Exception e) {

        }
    }

    /**
     * Меню
     * https://android--examples.blogspot.com/2016/01/android-how-to-use-options-menu.html
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    Terminal terminal;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String[] ProgramName = directory.replace(new Data().FEB_ONION_DIR, "").split("/");

        switch (id) {
            case R.id.loading:
                if (fileName == null || fileName.equals("")) {
                    if (ProgramName[0].endsWith("_project")) {
                        new NewFile(MainActivity.this, directory);
                    } else {
                        new NewProject(MainActivity.this);
                    }
                    Toast.makeText(MainActivity.this, "" + project_Name, Toast.LENGTH_SHORT).show();
                } else {
                    new Data().createFile(fileName, editText.getText().toString(), directory);
                    download(directory + ":" + fileName + ":" + project_Name + ":" + help.getConfiguration());
                    Toast.makeText(MainActivity.this, "" + help.getConfiguration(), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.new_file:
                editText.getText().clear();
                //  newSheet();
                new NewFile(MainActivity.this, directory);
                fileName = null;
                return true;
            case R.id.action_settings:
                new DialogSetting(MainActivity.this);
                return true;
            case R.id.open:
                try {
                    new Open(MainActivity.this, directory);
                } catch (Exception e) {

                }
                return true;
            case R.id.new_project:
                input = editText.getText().toString();
                new NewProject(MainActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * метот который перадает окну downaload путь к файлу и открывает окно downaload
     */
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void download(String comand) {
        // Intent intent = new Intent(this, download.class);
        //  intent.putExtra("downaload", comand);
        //  startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        terminal = new Terminal(MainActivity.this);
        terminal.setSetting(comand);
    }

    public void setEdit(String text) {
        editText.getText().insert(editText.getSelectionStart(), text);
    }

    public void show(String text) {
        Toast.makeText(MainActivity.this, "" + text, Toast.LENGTH_SHORT).show();
    }

    public void setTerminal(String message) {
        terminal.setTerminal(message);
    }

    public void setNumberCode(int progress) {
        numberCode.setTextSize(progress);
    }

    public void setTextSize(int progress) {
        editText.setTextSize(progress);
    }

    public void setFileName(String FileName) {
        this.fileName = FileName;
    }

    public void setDirectory(String Directory) {
        this.directory = Directory;
    }

    public void setEditText(String text) {
        editText.getText().clear();
        editText.append(text);
        editText.append("\n");
    }

    public void Information(String txt) {
        new Information(MainActivity.this, txt);
    }

    public void back() {
        if (editText.getSelectionStart() > 0) {
            editText.setSelection(editText.getSelectionEnd() - 1);
        } else {
        }
    }

    public void forward() {
        if (editText.getSelectionEnd() < editText.getText().toString().length()) {
            editText.setSelection(editText.getSelectionEnd() + 1);
        } else {
        }
    }


    /**
     * метод который принимает данные со второго активити
     * // форум в котоом можно прочитать про использваония этого метода
     * https://javarush.ru/groups/posts/regulyarnye-vyrazheniya-v-java регулятор віражения

     @RequiresApi(api = Build.VERSION_CODES.O)
     @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
     if (resultCode == RESULT_OK) {
     String message = data.getStringExtra(Intent.EXTRA_TEXT);
     if (message.equals("Excellent")) {

     } else {
     Pattern pattern = Pattern.compile("line(([^\\n+',']+))");
     Matcher matcher = pattern.matcher(message);
     while (matcher.find()) {
     String[] line = message.substring(matcher.start(), matcher.end()).split(" ");
     try {
     int l = Integer.parseInt(String.valueOf(new Scanner(line[1]).useDelimiter("[^\\d]+").nextInt()));
     if (l > 0) {
     NumberOfConstruction(l);

     } else {
     NumberOfConstruction(0);
     }
     } catch (NumberFormatException e) {
     }

     }
     }
     }
     }
     }
     */
    /**
     * метод который принимает данные со второго активити
     * // форум в котоом можно прочитать про использваония этого метода
     * https://javarush.ru/groups/posts/regulyarnye-vyrazheniya-v-java регулятор віражения
     */
    public void setReseult(String message) {
        if (message.equals("Excellent")) {

        } else {
            Pattern pattern = Pattern.compile("line(([^\\n+',']+))");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String[] line = message.substring(matcher.start(), matcher.end()).split(" ");
                try {
                    int l = Integer.parseInt(String.valueOf(new Scanner(line[1]).useDelimiter("[^\\d]+").nextInt()));
                    if (l > 0) {
                        numberOfConstruction(l);

                    } else {
                        numberOfConstruction(0);
                    }
                } catch (NumberFormatException e) {
                }

            }
        }
    }
}
