package com.example.testedit.terminal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.testedit.R;

import java.util.List;

public class TerminalAdapter extends ArrayAdapter<Console> {
    private List<Console> objects;
    private Context context;
    Console console;
    private int resourse;

    public TerminalAdapter(@NonNull Context context, int resourse, List<Console> objects) {

        super(context, resourse, objects);
        this.objects = objects;
        this.context = context;
        this.resourse = resourse;

    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(resourse, null);
        console = objects.get(position);
        EditText consoleEdit = view.findViewById(R.id.console);
        consoleEdit.setText(console.getConsole());

        return view;
    }
}
