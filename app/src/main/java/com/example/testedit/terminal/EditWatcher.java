package com.example.testedit.terminal;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class EditWatcher implements TextWatcher {
    Terminal terminal;

    public EditWatcher(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!s.toString().contains("\n")) {
            //terminal.shell(s.toString());

            // Log.e("TAG", "Первая строка: Печатается");
        } else {
            Log.e("TAG", "Первая строка: " + s.toString().startsWith("\n"));
            if (s.toString().startsWith("\n")) {
               terminal.shell("");
                terminal.clearEdit();

            } else {
                String[] str = s.toString().split("\n");
                terminal.shell(str[0]);
                terminal.clearEdit();
            }


        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
