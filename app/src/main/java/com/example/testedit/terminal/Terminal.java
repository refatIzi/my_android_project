package com.example.testedit.terminal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.testedit.MainInterface;
import com.example.testedit.PythonThread;
import com.example.testedit.R;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("ValidFragment")
public class Terminal extends Fragment implements View.OnClickListener {
    MainInterface mainInterface;
    Context context;
    private List<Console> consoleList = new ArrayList<>();
    Console console;
    TerminalAdapter adapter;
    ListView listView;
    EditText consoleEdit;
    InConsole inConsole;

    @SuppressLint("ValidFragment")
    public Terminal(Context context) {
        mainInterface = (MainInterface) context;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inConsole =new InConsole(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.loadLibrary("python3.11");
        System.loadLibrary("pythonthread");
        View v = inflater.inflate(R.layout.terminal_mode, container, false);
        listView = v.findViewById(R.id.consoleList);
        consoleEdit = v.findViewById(R.id.console);
        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(this);
        listView.setNestedScrollingEnabled(true);
        //nviron().get("PATH")
        consoleList.add(new Console(System.getProperties().toString()));


        adapter = new TerminalAdapter(context, R.layout.iteam_terminal, consoleList);

        listView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //тут пишете необходимые вещи в outState
        if (outState == null)
            outState = new Bundle();
        super.onSaveInstanceState(outState);
    }

    int i = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String command = consoleEdit.getText().toString();
                consoleList.add(new Console(command));
                adapter = new TerminalAdapter(context, R.layout.iteam_terminal, consoleList);
                listView.setAdapter(adapter);

                    //inConsole.shellExec(command);
                PythonThread lThread = new PythonThread(context);
                lThread.start();

                 //  inConsole.python();

                i++;
                break;

        }
    }

    public void shell(String line){
        consoleList.add(new Console(line));
        adapter = new TerminalAdapter(context, R.layout.iteam_terminal, consoleList);
        listView.setAdapter(adapter);
    }

}
