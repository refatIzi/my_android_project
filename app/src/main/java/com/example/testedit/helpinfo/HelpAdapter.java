package com.example.testedit.helpinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testedit.R;

import java.util.List;

public class HelpAdapter extends ArrayAdapter<HelpInfo> {
    private List<HelpInfo> objects;
    private Context context;
    HelpInfo helpInfo;
    private int resourse;

    public HelpAdapter(@NonNull Context context, int resourse, List<HelpInfo> objects) {

        super(context, resourse, objects);
        this.objects = objects;
        this.context = context;
        this.resourse = resourse;

    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(resourse, null);
        helpInfo = objects.get(position);
        TextView help = view.findViewById(R.id.help);
        help.setText(helpInfo.getHelp());
        TextView infirmation = view.findViewById(R.id.info);
        infirmation.setText(helpInfo.getInfirmation());

        return view;
    }


}
