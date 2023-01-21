package com.example.testedit.visualization;

import android.content.Context;
import android.os.AsyncTask;

import com.example.testedit.MainInterface;

/**
 * Watch для вывода нумерации строки при измен
 */
public class Watch extends AsyncTask<Void, Void, Void> {
    MainInterface mainInterface;

    public Watch(Context context) {
        mainInterface = (MainInterface) context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        try {
            mainInterface.numberOfConstruction(0);
        } catch (Exception e) {
        }
    }
}