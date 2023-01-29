package com.example.testedit.terminal;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.testedit.setting.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InConsole {
    Terminal terminal;
    public InConsole(Terminal terminal){
        this.terminal=terminal;
    }
    public  final String SHEL_EXECUTE_ERROR = "SHEL_EXECUTE_ERROR";
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void   shellExec(String cmdCommand) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final Process process = Runtime.getRuntime().exec( cmdCommand);
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
       terminal.shell(line);
                // stringBuilder.append(line);
            }

        } catch (Exception e) {
            terminal.shell(SHEL_EXECUTE_ERROR+" "+e);

        }

    }
    public void python()  {

        // I'm using the absolute path for my example.
        new Thread(new Runnable() {
            public void run(){
                try {
                    String fileName ="python3 - " +new Data().FEB_ONION_DIR+ "prog_project/Alphabetic_Order.py";

                    // Creates a ProcessBuilder
                    // doc: https://docs.oracle.com/javase/7/docs/api/java/lang/ProcessBuilder.html
                    ProcessBuilder pb = new ProcessBuilder( fileName);

                    pb.redirectErrorStream(false); // redirect error stream to a standard output stream
                    Process process  =pb.start();
                    // Reads the output stream of the process.
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line; // this will be used to read the output line by line. Helpful in troubleshooting.
                    while ((line = reader.readLine()) != null) {
                        //System.out.println(line);
                        terminal.shell(line);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "ZALUPA=" + e.toString());
                    throw new RuntimeException(e);
                }
            }
        }).start();






    }
}
