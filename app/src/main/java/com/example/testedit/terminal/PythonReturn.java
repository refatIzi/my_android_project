package com.example.testedit.terminal;

import android.content.Context;

public class PythonReturn extends Thread{
    Context context;
    Terminal terminal;
    public native String resultError();
    public native void clearError();

    public PythonReturn(Context context, Terminal terminal) {
        this.context=context;
        this.terminal=terminal;
        Return();
    }
    private void Return(){
        Thread result =new Thread(){
            @Override
            public void run() {
                while (true){
                    if(resultError().length()!=0){
                        //terminal.shell(resultError());
                        clearError();
                        break;
                    }
                }

            }
        };
        result.start();
    }
}
