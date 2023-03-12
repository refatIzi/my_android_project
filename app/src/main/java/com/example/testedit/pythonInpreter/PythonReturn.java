package com.example.testedit.pythonInpreter;

import android.content.Context;

import com.example.testedit.terminal.Terminal;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PythonReturn extends Thread {
    private static final String TAG = "PythonReturn";
    private final Logger mLogger;

    Context context;
    Terminal terminal;

    public native void returnInfoPython();

    public native boolean getStatusPy();

    public native boolean getStatusResult();

    public native boolean getStatusError();

    public native boolean getStatusErrorResult();

    public native String getResult();

    public native String getError();

    public native String resultError();

    public native void clearError();

    public PythonReturn(Context context, Terminal terminal) {
        mLogger = Logger.getLogger(TAG);
        this.context = context;
        this.terminal = terminal;
        returnPythonThread();
        //returnProcessing();
    }

    private void returnPythonThread() {
        terminal.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (getStatusPy()) {
                        //returnInfoPython();
                        terminal.shell(getResult());

                    } else if (getStatusError()) {
                        //returnInfoPython();
                        terminal.shell(getError());

                    }
                    if (getStatusResult()) {
                        break;
                    } else if (getStatusErrorResult()) {
                        break;
                    }
                    if (resultError().length() != 0) {
                        terminal.shell(resultError());
                        clearError();
                        break;
                    }
                }

            }
        });
    }

    private void returnProcessing() {
        terminal.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (resultError().length() != 0) {
                        terminal.shell(resultError());
                        clearError();
                        break;
                    }
                }
            }
        });

    }

    public void messageMe(String text) {

        terminal.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mLogger.log(Level.INFO, "INFO in NDK Android JAVA\n" + text);
                    terminal.shell(text);
                } catch (Exception e) {

                }
            }
        });
        //System.out.println(text);
    }
}
