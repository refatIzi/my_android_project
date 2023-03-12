package com.example.testedit;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testedit.date.Febo_Data;
import com.example.testedit.dialogwindows.DialogSetting;
import com.example.testedit.dialogwindows.Information;
import com.example.testedit.dialogwindows.NewFile;
import com.example.testedit.dialogwindows.NewProject;
import com.example.testedit.dialogwindows.Open;
import com.example.testedit.helpinfo.Help;
import com.example.testedit.permission.Permission;
import com.example.testedit.terminal.Terminal;
import com.example.testedit.visualization.Watch;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity implements MainInterface {

    EditText editText;
    TextView numberCode;
    String input;
    String fileName;
    private String DIRECTORY;
    String project_Name = "no project";
    Help help;
    Terminal terminal;
    FragmentTransaction fragmentHelper;
    FragmentTransaction fragmentTerminal;
    FrameLayout terminalFrameLayout;

    String ONION_DIR = new Febo_Data().FEB_ONION_DIR;

        /**
         * file editor. bootloader. output.
         * FEBO*/
    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Permission(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Watch(this).execute();
        numberCode = findViewById(R.id.numberCode);
        editText = findViewById(R.id.txtCode);
        terminalFrameLayout = findViewById(R.id.terminalMode);
        terminalFrameLayout.setVisibility(View.GONE);

        fragmentHelper = getFragmentManager().beginTransaction();
        help = new Help(MainActivity.this);
        fragmentHelper.replace(R.id.liner, help);
        fragmentHelper.commit();

        fragmentTerminal = getFragmentManager().beginTransaction();
        terminal = new Terminal(MainActivity.this);
        fragmentTerminal.replace(R.id.terminalMode, terminal);
        fragmentTerminal.commit();

        DIRECTORY = ONION_DIR;
        editText.setTextSize(new Febo_Data().setting().getTextSize());
        numberCode.setTextSize(new Febo_Data().setting().getTextSize());
        new Febo_Data().checkDirectory(ONION_DIR);
        new Open(MainActivity.this, DIRECTORY);

        editText.addTextChangedListener(new ActivityTextWatcher(this));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.loading:
                download();
                return true;
            case R.id.new_file:
                editText.getText().clear();
                new NewFile(MainActivity.this, DIRECTORY);
                terminalFrameLayout.setVisibility(View.GONE);
                fileName = null;
                return true;
            case R.id.action_settings:
                terminalFrameLayout.setVisibility(View.GONE);
                new DialogSetting(MainActivity.this);
                return true;
            case R.id.open:
                terminalFrameLayout.setVisibility(View.GONE);
                new Open(MainActivity.this, DIRECTORY);
                return true;
            case R.id.new_project:
                terminalFrameLayout.setVisibility(View.GONE);
                input = editText.getText().toString();
                new NewProject(MainActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean stus = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void download() {
        if (fileName == null || fileName.equals("")) {
            if (new Febo_Data().checkProject(DIRECTORY)) {
                new NewFile(MainActivity.this, DIRECTORY);
            } else {
                new NewProject(MainActivity.this);
            }
        } else {
            new Febo_Data().createFile(fileName, editText.getText().toString(), DIRECTORY);
            Toast.makeText(this, ""+DIRECTORY+fileName, Toast.LENGTH_SHORT).show();
            //terminal = new Terminal(MainActivity.this);
            // terminal.setSetting(DIRECTORY + ":" + fileName + ":" + project_Name + ":" + help.getConfiguration());
            //Intent intent = new Intent(this, Download.class);
            //intent.putExtra("downaload", DIRECTORY + ":" + fileName + ":" + project_Name + ":" + help.getConfiguration());
            // ((Activity) this).startActivityForResult(intent, 0);
            //  final int SECOND_ACTIVITY_REQUEST_CODE = 0;
            // Intent intent = new Intent(this, download.class);
            //  intent.putExtra("downaload", comand);
            //  startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            checkTerminal();
        }

    }
public void checkTerminal(){
    if (terminalFrameLayout.getVisibility()==View.GONE) {
        terminalFrameLayout.setVisibility(View.VISIBLE);
        terminal.setDirectory(DIRECTORY);
        terminal.setFile(fileName);
        terminal.compileTerminal();
    } else {
        terminalFrameLayout.setVisibility(View.GONE);
    }
}
    public void setEdit(String text) {
        editText.getText().insert(editText.getSelectionStart(), text);
    }

    public void show(String text) {
        Toast.makeText(MainActivity.this, "" + text, Toast.LENGTH_SHORT).show();
    }

    public void setTerminal(String message) {
        //terminal.setTerminal(message);
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

    public void setDIRECTORY(String Directory) {
        this.DIRECTORY = Directory;
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
    public void setResult(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

        if (message.equals("Excellent")) {

        } else {
            message=message.replaceAll("SyntaxError\\(\"expected","");
            Pattern pattern = Pattern.compile("line(([^\\n+',']+))");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String[] line = message.substring(matcher.start(), matcher.end()).split(" ");
                Toast.makeText(this, ""+line, Toast.LENGTH_SHORT).show();

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
