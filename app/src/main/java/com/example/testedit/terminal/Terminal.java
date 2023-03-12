package com.example.testedit.terminal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.termux.TermuxConstants.TERMUX_APP.RUN_COMMAND_SERVICE;

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
    TerminalAdapter adapter;
    ListView listView;
    EditText consoleEdit;
    InConsole inConsole;
    String directory;
    String pythonFile;


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
        listView = v.findViewById(R.id.consoleList);
        consoleEdit = v.findViewById(R.id.console);
        consoleEdit.addTextChangedListener(new EditWatcher(this));
        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(this);
        listView.setNestedScrollingEnabled(true);

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
             runB();
                //compileTerminal();
        }
    }
    public void runA(){
        String termuxPackageName = "com.termux";
        String termuxActivityName = termuxPackageName + ".com.termux.app.RunCommandService";
        String command = "echo 'Hello, World!'";

        Intent intent = new Intent();
        intent.setClassName("com.termux", "com.termux.app.RunCommandService");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("com.termux.RUN_COMMAND");
        intent.putExtra("com.termux.execute.EXTRA_COMMAND", command);
        intent.setPackage("com.termux");

        //startActivity(intent);
        getActivity().startService(intent);
        getActivity().startActivity(intent);
        //context.sendBroadcast(intent);

    }
    public void runB(){


        String LOG_TAG = "MainActivity";

        Intent intent = new Intent();
        intent.setClassName(TermuxConstants.TERMUX_PACKAGE_NAME, TermuxConstants.TERMUX_APP.RUN_COMMAND_SERVICE_NAME);
        intent.setAction(RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND);
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_COMMAND_PATH, "/data/data/com.termux/files/usr/bin/top");
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_ARGUMENTS, new String[]{"-n", "2"});
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_WORKDIR, "/data/data/com.termux/files/home");
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_BACKGROUND, false);
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_SESSION_ACTION, "0");
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_COMMAND_LABEL, "top command");
        intent.putExtra(RUN_COMMAND_SERVICE.EXTRA_COMMAND_DESCRIPTION, "Runs the top command to show processes using the most resources.");

// Create the intent for the IntentService class that should be sent the result by TermuxService
        Intent pluginResultsServiceIntent = new Intent(context, PluginResultsService.class);

// Generate a unique execution id for this execution command
        int executionId = PluginResultsService.getNextExecutionId();

// Optional put an extra that uniquely identifies the command internally for your app.
// This can be an Intent extra as well with more extras instead of just an int.
        pluginResultsServiceIntent.putExtra(PluginResultsService.EXTRA_EXECUTION_ID, executionId);

// Create the PendingIntent that will be used by TermuxService to send result of
// commands back to the IntentService
// Note that the requestCode (currently executionId) must be unique for each pending
// intent, even if extras are different, otherwise only the result of only the first
// execution will be returned since pending intent will be cancelled by android
// after the first result has been sent back via the pending intent and termux
// will not be able to send more.
        PendingIntent pendingIntent = PendingIntent.getService(context, executionId, pluginResultsServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        intent.putExtra(TermuxConstants.TERMUX_APP.RUN_COMMAND_SERVICE.EXTRA_PENDING_INTENT, pendingIntent);

        try {
            // Send command intent for execution
            Log.d(LOG_TAG, "Sending execution command with id " + executionId);
            getActivity().startService(intent);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to start execution command with id " + executionId + ": " + e.getMessage());
        }
    }


    public void clearEdit(){
        consoleEdit.setText("");
    }

    public void compileTerminal() {
        consoleList.clear();
        listView.setAdapter(null);
        //shell("");
       /* new PythonThread(
                context,
                this,
                directory,
                pythonFile
        ).start();

        */






    }

    public void shell(String line) {
        List<String> tokens = new ArrayList<String>();
        tokens.add("line");
        tokens.add("Traceback");
        tokens.add("NameError");
        tokens.add("SyntaxError");
        String join=TextUtils.join("|", tokens);
        String patternString = "\\b(" + join + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            mainInterface.setResult(line);
        }
        setShellOut(line);
    }
    private void setShellOut(String line){
        consoleList.add(new Console(line));
        adapter = new TerminalAdapter(context,this, R.layout.iteam_terminal, consoleList);
        listView.setAdapter(adapter);
    }


}
