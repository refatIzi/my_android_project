package com.example.testedit.pythonInpreter;


import android.app.Activity;
import android.content.Context;
import android.system.ErrnoException;
import android.system.Os;

import com.example.testedit.date.Febo_Data;
import com.example.testedit.terminal.Terminal;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PythonThread extends Thread {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = "PythonThread";

    // JNI links
    public native int initPython(String aPath);

    public native int runPython(String aFilename);

    public native int cleanupPython();

    private final Logger mLogger;
    private final Context mContext;
    Activity activity;
    Terminal terminal;
    private String directory;
    private String pythonFile;

    public PythonThread(Context aContext, Terminal terminal,String directory, String pythonFile) {
        mLogger = Logger.getLogger(TAG);
        mContext = aContext;
        activity = (Activity) mContext;
        this.terminal = terminal;
        this.directory=directory;
        this.pythonFile = pythonFile;
        if (pythonFile.isEmpty()) {

        } else {
            initializePython();
            new PythonReturn(aContext, terminal).start();
        }
    }

    public void runFile(String pythonFile) {
        this.pythonFile = pythonFile;
    }

    @SuppressWarnings("UnusedReturnValue")
    private int initializePython() {
        mLogger.log(Level.INFO, "We are in initialize inside of the service");

        String lTempPath = Common.getEngineRootDirectory(mContext);

        // Unzip the right folder for he processor
        if (Common.isEmulator()) {
            // Extract our 64bit zip
            Febo_Data.unzipFileFromAssets(mContext, "Pythonx86_64.zip");
            lTempPath += "Pythonx86_64";
        } else if (Common.is64bitProcessor()) {
            // Extract our 64bit zip
            Febo_Data.unzipFileFromAssets(mContext, "Python64.zip");
            lTempPath += "Python64";
        } else {
            Febo_Data.unzipFileFromAssets(mContext, "Python32.zip");
            lTempPath += "Python32";
        }

        // Put the python files where we can execute them
        //Febo_Data.copyFilesFromAssets(mContext, pythonFile);

        final String lPythonRootPath = lTempPath;

        // run a python file
        Thread lPythonThread = new Thread() {
            public void run() {
                this.setName("PythonEngine");     // Give our thread a friendly name we can debug with.

                // set all the environment variables for python to use
                try {
                    Os.setenv("NumberToWrite", "104", true);
                } catch (ErrnoException e) {
                    mLogger.log(Level.SEVERE, "Could not set environment variables for Python");
                }

                // initialize python
                int lPythonReturn = initPython(lPythonRootPath);

                // Make sure that we initialized cleanly
                if (lPythonReturn < 0) {
                    mLogger.log(Level.INFO, "Failed to initialize Python. Possible Python corruption. Python will be deleted and re-downloaded");
                    return;
                }

                //File lPathToMain = new File(Common.getEngineRootDirectory(mContext) + "/factori.py");
                mLogger.log(Level.SEVERE, "DIRECTORY FILE=" +directory+pythonFile);

               // mLogger.log(Level.SEVERE, "DIRECTORY FILE=" + new Febo_Data().FEB_ONION_DIR + "prog_project/Start.py");
                File lPathToMain = new File(directory+pythonFile);

                // Make sure that the file exists0
                if (!lPathToMain.exists()) {
                    mLogger.log(Level.SEVERE, "Unable to run the Python Level 3.  The "+lPathToMain+" does not exist in proper location.  File Location=" + lPathToMain.getAbsolutePath());
                    return;
                }

                try {
                    // Run our main
                    runPython(lPathToMain.getAbsolutePath());
                    // We are done running, we will init again, cleanup
                    cleanupPython();
                } catch (Exception e) {
                    mLogger.log(Level.SEVERE, "!@!@!@!@ Our thread was interrupted!!!");
                }
            }
        };
        // Start this thread
        lPythonThread.start();

        return 0;
    }


}
