package com.example.testedit.terminal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.testedit.R;

import java.util.List;

public class LineAdapter extends ArrayAdapter<Line> {

    Context context;
    Terminal terminal;
    private int resource;
    List<Line> lines;
    Line line;

    public LineAdapter( Context context, Terminal terminal, int resource, List<Line> lines) {
        super(context, resource,lines);
        this.lines = lines;
        this.context = context;
        this.terminal = terminal;
        this.resource = resource;



    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(resource, null);
        line = lines.get(position);
        EditText consoleEdit = view.findViewById(R.id.lineConsole);
        consoleEdit.setText(line.getLine());
        return view;
    }
}
