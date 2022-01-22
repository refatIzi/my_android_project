package com.example.testedit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.testedit.dialogwindows.NewFile;
import com.example.testedit.dialogwindows.NewProject;
import com.example.testedit.dialogwindows.Open;
import com.example.testedit.dialogwindows.Setting;
import com.example.testedit.dialogwindows.Terminal;
import com.example.testedit.search.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements MainInterface {


    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText editText;
    TextView numberCode;
    ImageButton tab, sc_1, sc_2, divide, procent, hashtag, plas, minus, equals, down_left, down_right;
    String input;
    String FileName = "";
    orientation orientation;
    private String Directory;
    private List<Search> mLista = new ArrayList<>();
    String project_Name = "no project";


    public final String[] EXTERNAL_PERMS = {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 42;

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }


    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (!checkPermission()) {
            requestPermission();
        }

        /**Запрет на поворот экрана*/
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /**разрешения  которые я тут исползовал*/

        orientation = new orientation();
        orientation.execute();
        numberCode = findViewById(R.id.numberCode);
        editText = findViewById(R.id.txtCode);


     //   tab = findViewById(R.id.Table);
     //   sc_1 = findViewById(R.id.sc_1);
      //  sc_2 = findViewById(R.id.sc_2);
      //  divide = findViewById(R.id.divide);
     //   procent = findViewById(R.id.procent);
     //   hashtag = findViewById(R.id.hashtag);
     //   plas = findViewById(R.id.plas);
     //   minus = findViewById(R.id.minus);
     //   equals = findViewById(R.id.equals);
     //   down_left = findViewById(R.id.down_left);
      //  down_right = findViewById(R.id.down_right);

     //   tab.setOnClickListener(this);
     //   sc_1.setOnClickListener(this);
     //   sc_2.setOnClickListener(this);
     //   divide.setOnClickListener(this);
     //   procent.setOnClickListener(this);
     //   hashtag.setOnClickListener(this);
     //   plas.setOnClickListener(this);
      //  equals.setOnClickListener(this);
       // down_left.setOnClickListener(this);
      //  down_right.setOnClickListener(this);

        FragmentTransaction fTrans= getFragmentManager().beginTransaction();
        button buttonfr=new button();
        fTrans.replace(R.id.liner, buttonfr);
        fTrans.commit();
        Directory = Environment.getExternalStorageDirectory() + "/python/";

        File dir = new File(Directory);
        if (!dir.exists()) {
            //   Toast.makeText(MainActivity.this, "create folder", Toast.LENGTH_LONG).show();
            dir.mkdir();
            Work_with_File.saveFile("connect.txt", "192.168.3.1:22:root:password:home/Document/python:puthon3:15",
                    Environment.getExternalStorageDirectory().toString() + "/python/");
            Work_with_File.saveFile("HelloWorld.py", "print('Hello World')", Environment.getExternalStorageDirectory().toString() + "/python/");
        } else {
            /**Считываем дданные о размере фаила и применяем их для нумерации строки в numberCode
             * и в editText*/
            String[] separated;
            String info = Work_with_File.readInformation("connect.txt", "", Environment.getExternalStorageDirectory().toString() + "/python/");
            separated = info.split(":");
            if (separated.length > 6) {
                editText.setTextSize(Integer.parseInt(separated[6]));
                numberCode.setTextSize(Integer.parseInt(separated[6]));
            }
            new Open(MainActivity.this, Directory);

        }
        editText.addTextChangedListener(new TextWatcher() {

            ColorScheme keywords1 = new ColorScheme(
                    Pattern.compile(
                            "\\b(self|def|as|assert|break|continue|del|elif|else|except|finally|for|from|global|if|import|in|pass|raise|return|try|while|with|yield)\\b"),
                    Color.parseColor("#c56a77")


            );
            ColorScheme keywords2 = new ColorScheme(
                    Pattern.compile(
                            "\\b(False|None|True|and|nonlocal|not|or|class|def|is|lambda)\\b"),
                    Color.parseColor("#3e9cca")


            );

            ColorScheme numbers = new ColorScheme(
                    Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)"),
                    Color.parseColor("#2f5f93")
            );
            //Built-in functions1 Встроенные функции 1
            ColorScheme Built_in_functions1 = new ColorScheme(
                    Pattern.compile("(\\b(passive|Options|dict()|slice()|object()|staticmethod()|str()|int()|bool()|super()|tuple()|bytearray()|float()|bytes()|type()|property()|list()|frozenset()|classmethod()|complex()|set())\\b)"),
                    Color.parseColor("#2aa9b0")
            );
            //Built-in functions2 Встроенные функции 2
            ColorScheme Built_in_functions2 = new ColorScheme(
                    Pattern.compile("(\\b(min()|setattr()|abs()|all()|dir()|hex()|next()|any()|divmod()|id()|sorted()|ascii()|enumerate()|input()|oct()|max()|round()|\n" +
                            "bin()|eval()|exec()|isinstance()|ord()|sum()|filter()|issubclass()|pow()|iter()|print()|callable()|format()|delattr()|\n" +
                            "len()|chr()|range()|vars()|getattr()|locals()|repr()|zip()compile()|globals()|map()|reversed()|__import__()|hasattr()|hash()|memoryview())\\b)"),
                    Color.parseColor("#cc7832")
            );

            ColorScheme String_methods = new ColorScheme(
                    Pattern.compile("(\\b(capitalize()|casefold()|center()|count()|encode()|endswith()|expandtabs()|find()|index()|isalnum()\n" +
                            "isalpha()|isascii()|isdigit()|isidentifier()|islower()|isnumeric()|isprintable()|isspace()\n" +
                            "istitle()|isupper()|join()|ljust()|lower()|lstrip()|rstrip()|maketrans()|partition()|replace()\n" +
                            "rfind()|rindex()|rjust()|rpartition()|rsplit()|split()|splitlines()|startswith()|strip()\n" +
                            "swapcase()|title()|translate()|upper()|zfill())\\b)"),
                    Color.parseColor("#b3b102")
            );

            ColorScheme List_methods = new ColorScheme(
                    Pattern.compile("(\\b(append()|extend()|insert()|remove()|pop()|clear()|sort()|reverse()|copy())\\b)"),
                    Color.parseColor("#b3b102")
            );

            ColorScheme Dictionary_methods = new ColorScheme(
                    Pattern.compile("(\\b(fromkeys()|get()|items()|keys()|popitem()|setdefault()|update()|values())\\b)"),
                    Color.parseColor("#b3b102")

            );

            ColorScheme Working_with_files = new ColorScheme(
                    Pattern.compile("(\\b(read()|write()|tell()|seek()|close()|open()|closed|mode|name|softspace)\\b)"),
                    Color.parseColor("#b3b102")
            );

            ColorScheme argument = new ColorScheme(
                    Pattern.compile("(\\b(file_name|access_mode|Buffering)\\b)"),
                    Color.parseColor("#784fae")
            );

            ColorScheme sign = new ColorScheme(
                    Pattern.compile("\\#(.*[\\n]+|$)"),
                    Color.parseColor("#627b57")
            );
            /**Регулятор с 2 # для выделения света текс и вывода инфомрауии о коде в проводнике*/
            ColorScheme information = new ColorScheme(
                    Pattern.compile("\\##(.*[\\n]+|$)"),
                    Color.parseColor("#ab7e00")
            );
            ColorScheme brackets = new ColorScheme(
                    Pattern.compile("[\\(\\)]"),
                    Color.parseColor("#3e9cca")
            );
            ColorScheme squarebrackets = new ColorScheme(
                    Pattern.compile("[\\[\\]]"),
                    Color.parseColor("#3e9cca")
            );
            ColorScheme braces = new ColorScheme(
                    Pattern.compile("[\\{\\}]"),
                    Color.parseColor("#3e9cca")
            );
            /**Регулятор для трех и менее ковычек*/
            ColorScheme kovichki = new ColorScheme(
                    Pattern.compile("\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\""),
                    Color.parseColor("#b4794c")
            );
            /**регулятор для подсвтеки одиночных букв*/
            ColorScheme letters = new ColorScheme(
                    Pattern.compile("(\\b(q|w|e|r|t|y|u|i|o|p|a|s|d|f|g|h|j|k|l|z|x|c|v|b|n|m|Q|W|E|R|T|Y|U|I|O|P|A|S|D|F|G|H|J|K|L|Z|X|C|V|B|N|M)\\b)"),
                    Color.parseColor("#648cb8")
            );
            final ColorScheme[] schemes = {keywords1, keywords2, numbers, Built_in_functions1, Built_in_functions2,
                    String_methods, List_methods, Dictionary_methods, letters, Working_with_files, argument, sign,
                    kovichki, information, brackets, squarebrackets, braces};


            //до изменении текста
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**при изменении текста и добавлениии текста и переходе на новую строку*/
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NumberOfConstruction(0);

            }

            private boolean isReached = false;

            // после изменении текста
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
        String[] text={"if","else","for","while","iff","forewer"};
        /**следим за вводом текста*/

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
              // Accept only letter & digits ; otherwise just return
                if(source.length()>0) {
                    for (int i = 0; i < text.length; i++) {
                        if (text[i].startsWith(source.toString()))
                            Toast.makeText(MainActivity.this, text[i] + " ~ " + source.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                    //    if(source.toString().equals("if"))
                    //    Toast.makeText(MainActivity.this,""+source.toString().length(),Toast.LENGTH_SHORT).show();

                return null;
            }

        };

        editText.setFilters(new InputFilter[] { filter });
    }


    /**
     * Кнопки нижней вкладки с помошью которого мы можем добовлять знаки
     */
   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //tab,sc_1,sc_2,divide,procent,hashtag,plas,minus,equals,down_left,down_right;
            case R.id.Table:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
              //  editText.getText().insert(editText.getSelectionStart(), "    ");
                //  Toast.makeText(getApplicationContext(), sesion+" ", Toast.LENGTH_LONG).show();
           //     break;
           // case R.id.sc_1:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
           //     editText.getText().insert(editText.getSelectionStart(), "<");
          //      break;
          //  case R.id.sc_2:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
           //     editText.getText().insert(editText.getSelectionStart(), ">");
            //    break;
           // case R.id.divide:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
           //     editText.getText().insert(editText.getSelectionStart(), "/");
            //    break;
           // case R.id.procent:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
            //    editText.getText().insert(editText.getSelectionStart(), "%");
          //      break;
          //  case R.id.hashtag:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
         //       editText.getText().insert(editText.getSelectionStart(), "#");
         //       break;
         //   case R.id.plas:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
          //      editText.getText().insert(editText.getSelectionStart(), "+");
         //       break;
          //  case R.id.minus:
          //      /**метод смомошью которого можно добавлять втекушую позицию текст*/
         //       editText.getText().insert(editText.getSelectionStart(), "-");
         //       break;
         //   case R.id.equals:
         //       /**метод смомошью которого можно добавлять втекушую позицию текст*/
         //       editText.getText().insert(editText.getSelectionStart(), "=");
        //        break;
       //     case R.id.down_left:
        //        /**метод смомошью которого можно добавлять втекушую позицию текст*/
//
        //        if (editText.getSelectionStart() > 0) {
             //       editText.setSelection(editText.getSelectionEnd() - 1);
          //      } else {
                    //start of string, cannot move cursor backward
         ////       }
          //      break;
          //  case R.id.down_right:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
         //       if (editText.getSelectionEnd() < editText.getText().toString().length()) {
           //         editText.setSelection(editText.getSelectionEnd() + 1);
          //      } else {
          //          //end of string, cannot move cursor forward
          //      }
         //       break;
       // }
   // }

    /**
     * создаем AsyncTask для вывода нумерации строки при измен
     */
    class orientation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                NumberOfConstruction(0);


            } catch (Exception e) {
            }
            /**Проверяяем ориентацию экрана*/
        }
    }

    /**
     * Метод котрый выводит номурецию строки
     */
    private void NumberOfConstruction(int errLine) {
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
        String[] ProgramName = Directory.replace(Environment.getExternalStorageDirectory().toString() + "/python/", "").split("/");

        switch (id) {
            case R.id.loading:
                if (FileName == null || FileName.equals("")) {
                    if (ProgramName[0].endsWith("_project")) {
                        new NewFile(MainActivity.this, Directory);
                    } else {
                        new NewProject(MainActivity.this);
                    }

                    Toast.makeText(MainActivity.this, "" + project_Name, Toast.LENGTH_SHORT).show();
                } else {
                    //   Toast.makeText(MainActivity.this,FileName,Toast.LENGTH_LONG).show();
                    // File Path = new File(Directory);
                    Work_with_File.saveFile(FileName, editText.getText().toString(), Directory);
                    //  Path = new File(Path.getAbsolutePath());
                    download(Directory + ":" + FileName + ":" + project_Name);

                    Toast.makeText(MainActivity.this, "" + project_Name, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.new_file:
                editText.getText().clear();
                //  newSheet();
                new NewFile(MainActivity.this, Directory);
                FileName = null;
                return true;
            case R.id.action_settings:
                new Setting(MainActivity.this);
                return true;
            case R.id.open:
                try {
                    new Open(MainActivity.this, Directory);
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
        this.FileName = FileName;
    }

    public void setDirectory(String Directory) {
        this.Directory = Directory;
    }

    public void setEditText(String text) {
        editText.getText().clear();
        editText.append(text);
        editText.append("\n");
    }


    /**
     * метод который принимает данные со второго активити
     * // форум в котоом можно прочитать про использваония этого метода
     * https://javarush.ru/groups/posts/regulyarnye-vyrazheniya-v-java регулятор віражения

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
