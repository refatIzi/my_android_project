package com.example.testedit.terminal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;
import android.widget.TextView;


import com.example.testedit.MainInterface;
import com.example.testedit.R;

@SuppressLint("ValidFragment")
public class Terminal extends Fragment implements View.OnClickListener {
    MainInterface mainInterface;
    Context context;
    int i=0;

    @SuppressLint("ValidFragment")
    public Terminal(Context context) {
        mainInterface = (MainInterface) context;
        this.context = context;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_mode, container, false);
        textView = v.findViewById(R.id.show);
        Button button = v.findViewById(R.id.butt);
        textView.append(""+i);
        button.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butt:
                i++;
                textView.append(""+i);

                break;
        }
    }
}
