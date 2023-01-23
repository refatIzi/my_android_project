package com.example.testedit.terminal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.testedit.MainInterface;
import com.example.testedit.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Terminal extends Fragment implements View.OnClickListener {
    MainInterface mainInterface;
    Context context;
    private List<Console> consoleList = new ArrayList<>();
    Console console;
    TerminalAdapter adapter;
    ListView listView;

    @SuppressLint("ValidFragment")
    public Terminal(Context context) {
        mainInterface = (MainInterface) context;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminal_mode, container, false);
        listView = v.findViewById(R.id.consoleList);
        Button button=v.findViewById(R.id.button);
        button.setOnClickListener(this);

        consoleList.add(new Console("Hello body"));


        adapter = new TerminalAdapter(context, R.layout.iteam_terminal, consoleList);

        listView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        //тут пишете необходимые вещи в outState
        if(outState == null)
            outState = new Bundle();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                consoleList.add(new Console("Hello body"));
                adapter = new TerminalAdapter(context, R.layout.iteam_terminal, consoleList);

                listView.setAdapter(adapter);
                break;

        }
    }
}
