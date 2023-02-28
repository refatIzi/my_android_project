package com.example.testedit;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;

import com.example.testedit.terminal.PythonReturn;
import com.example.testedit.terminal.Terminal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PythonThread extends Thread {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = "PythonThread";

    // JNI links
    public native int initPython(String aPath);

    public native int runPython(String aFilename);

    public native int cleanupPython();

    public native void returnInfoPython();

    public native boolean getStatusPy();

    public native boolean getStatusResult();

    public native boolean getStatusError();

    public native boolean getStatusErrorResult();
    public native String getResult();
    public native String getError();


    private final Logger mLogger;
    private final Context mContext;
    Activity activity;
    Terminal terminal;

    public PythonThread(Context aContext, Terminal terminal) {
        mLogger = Logger.getLogger(TAG);
        mContext = aContext;
        activity = (Activity) mContext;
        this.terminal = terminal;

        initializePython();
        //CatTask catTask = new CatTask();
        //catTask.execute();
        info();
        PythonReturn pythonReturn=new PythonReturn(aContext,terminal);
        pythonReturn.start();
        //Return();
    }


    public void messageMe(String text) {

        activity.runOnUiThread(new Runnable() {
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

    @SuppressWarnings("UnusedReturnValue")
    private int initializePython() {
        mLogger.log(Level.INFO, "We are in initialize inside of the service");

        String lTempPath = Common.getEngineRootDirectory(mContext);

        // Unzip the right folder for he processor
        if (Common.isEmulator()) {
            // Extract our 64bit zip
            unzipFileFromAssets("Pythonx86_64.zip");
            lTempPath += "Pythonx86_64";
        } else if (Common.is64bitProcessor()) {
            // Extract our 64bit zip
            unzipFileFromAssets("Python64.zip");
            lTempPath += "Python64";
        } else {
            unzipFileFromAssets("Python32.zip");
            lTempPath += "Python32";
        }

        // Put the python files where we can execute them
        copyPythonFilesFromAssets("factorial.py");

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

                File lPathToMain = new File(Common.getEngineRootDirectory(mContext) + "/factorial.py");

                // Make sure that the file exists0
                if (!lPathToMain.exists()) {
                    mLogger.log(Level.SEVERE, "Unable to run the Python Level 3.  The main.py does not exist in proper location.  File Location=" + lPathToMain.getAbsolutePath());
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

    public void setCommand(String string) {
        try {
        } catch (Exception e) {

        }

    }

    private void copyPythonFilesFromAssets(String aFileName) {
        AssetManager assetManager = mContext.getAssets();

        InputStream lInputStream;
        OutputStream lOutputStream;
        try {
            lInputStream = assetManager.open(aFileName, AssetManager.ACCESS_BUFFER);

            String outDir = Common.getEngineRootDirectory(mContext);

            File outFile = new File(outDir, aFileName);

            lOutputStream = new FileOutputStream(outFile);
            copyFile(lInputStream, lOutputStream);
            lInputStream.close();
            lOutputStream.flush();
            lOutputStream.close();
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + aFileName, e);
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void unzipFileFromAssets(String aSourceFile) {
        AssetManager assetManager = mContext.getAssets();

        InputStream lInputStream = null;
        try {
            lInputStream = assetManager.open(aSourceFile, AssetManager.ACCESS_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String lDestinationFile = mContext.getExternalFilesDir(null) + \"\\\\Python" + aSourceFile;
        String lDestinationFile = Common.getEngineRootDirectory(mContext);

        ZipInputStream zis;
        try {
            // ensure the destination directory exists
            if (!new File(lDestinationFile).mkdirs()) {
                mLogger.log(Level.INFO, "Unable to create directory '" + lDestinationFile + "'. It probably already exists");
            }

            // get the input stream of the zip file itself
            assert lInputStream != null;
            zis = new ZipInputStream(new BufferedInputStream(lInputStream));
            ZipEntry ze;
            byte[] buffer = new byte[8192];
            int count;

            // loop through all the zip entries in the zip
            while ((ze = zis.getNextEntry()) != null) {
                // if this entry is simply a directory, just create it and move on
                if (ze.isDirectory()) {
                    String dir = Common.ensureStringEndsWithForwardslash(lDestinationFile + File.separator + ze.getName());
                    File fmd = new File(dir);
                    if (!fmd.exists()) {
                        // directory doesn't exist yet, try to create it
                        if (!fmd.mkdirs()) {
                            mLogger.log(Level.WARNING, "Unable to create directory on device from zip: " + dir + ", DirExistsAlready=" + fmd.exists());
                        }
                    }
                    continue;
                }

                // if we get here, we're dealing with a straight file
                File destFile = new File(lDestinationFile, ze.getName());
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed for the file
                assert destinationParent != null;
                if (!destinationParent.isDirectory() && !destinationParent.mkdirs()) {
                    mLogger.log(Level.WARNING, "Unable to create directory " + destinationParent);
                }

                // write the file
                FileOutputStream lFileStream = new FileOutputStream(lDestinationFile + File.separator + ze.getName());
                while ((count = zis.read(buffer)) != -1) {
                    lFileStream.write(buffer, 0, count);
                }

                lFileStream.close();
                zis.closeEntry();
            }

            zis.close();

            mLogger.log(Level.INFO, aSourceFile + " unzipped successfully");
        } catch (Exception e) {
            mLogger.log(Level.SEVERE, "Exception while unzipping file:" + e.getMessage());
        }
    }

    public void info(){
        terminal.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (getStatusPy()) {
                        //returnInfoPython();
                        terminal.shell(getResult());

                    }
                    else if (getStatusError()) {
                        //returnInfoPython();
                        terminal.shell(getError());

                    }
                    if (getStatusResult()) {
                        break;
                    }
                    else if (getStatusErrorResult()) {
                        break;
                    }
                }

            }
        });
    }
    public void infoThread() {

                new Thread() {
                    @Override
                    public void run() {

                        while (true) {
                            if (getStatusPy()) {
                                //returnInfoPython();
                                terminal.shell(getResult());

                            }
                            else if (getStatusError()) {
                                //returnInfoPython();
                                terminal.shell(getError());

                            }
                            if (getStatusResult()) {
                                break;
                            }
                            else if (getStatusErrorResult()) {
                                break;
                            }
                        }

                    }
                }.start();





    }

    class CatTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                if (getStatusPy()) {
                    //returnInfoPython();

                    terminal.shell(getResult());

                }
                else if (getStatusError()) {
                    //returnInfoPython();
                    terminal.shell(getError());

                }
                if (getStatusResult()) {
                    break;
                }
                else if (getStatusErrorResult()) {
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
