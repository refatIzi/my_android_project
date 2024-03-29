package com.example.testedit.terminal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.pythonInpreter.PythonThread;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("ValidFragment")
public class Terminal extends Fragment implements View.OnClickListener {
    MainInterface mainInterface;
    Context context;
    private List<Console> consoleList = new ArrayList<>();
    Console console;
    TerminalAdapter terminalAdapter;
    ListView terminalView;
    ListView lineView;
    EditText consoleEdit;
    InConsole inConsole;
    String directory;
    String pythonFile;
    int lineID = 1;


    @SuppressLint("ValidFragment")
    public Terminal(Context context) {
        mainInterface = (MainInterface) context;
        this.context = context;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setFile(String pythonFile) {
        this.pythonFile = pythonFile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inConsole = new InConsole(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.loadLibrary("python3.11");
        System.loadLibrary("pythonthread");
        View v = inflater.inflate(R.layout.terminal_mode, container, false);
        terminalView = v.findViewById(R.id.consoleList);
        consoleEdit = v.findViewById(R.id.console);
        consoleEdit.addTextChangedListener(new EditWatcher(this));
        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(this);
        terminalView.setNestedScrollingEnabled(true);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //тут пишете необходимые вещи в outState
        if (outState == null)
            outState = new Bundle();
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String command = consoleEdit.getText().toString();
                //shell(command);
                //compileTerminal();

        }
    }


    public void clearEdit() {
        consoleEdit.setText("");
    }

    public void compileTerminal() {
        lineID = 1;
        consoleList.clear();
        terminalView.setAdapter(null);
        new PythonThread(
                context,
                this,
                directory,
                pythonFile
        ).start();
    }

    public void shell(String line) {
        List<String> tokens = new ArrayList<String>();
        tokens.add("line");
        tokens.add("Traceback");
        tokens.add("NameError");
        tokens.add("SyntaxError");
        String join = TextUtils.join("|", tokens);
        String patternString = "\\b(" + join + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            mainInterface.setResult(line);
        }
        setShellOut(line);
    }



    private void setShellOut(String line) {
        consoleList.add(new Console(line));
        terminalAdapter = new TerminalAdapter(context, this, R.layout.iteam_terminal, consoleList);
        terminalView.setAdapter(terminalAdapter);
        lineID++;
    }


}
