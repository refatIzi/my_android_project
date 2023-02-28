package com.example.testedit.rtrs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testedit.R;
import com.example.testedit.terminal.Console;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

   // private List<RecyclerItem> listItems;
    Context mContext;
    Console console;
    private List<Console> objects;

    public MyAdapter(List<Console> objects, Context mContext) {
        this.objects = objects;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_terminal, parent, false);
        return new ViewHolder(v);
    }
    int position;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.position=position;


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText consoleEdit;
        public ViewHolder(View itemView) {
            super(itemView);
            consoleEdit = itemView.findViewById(R.id.console);
            console  = objects.get(position);
            if(console.getConsole().isEmpty())
            consoleEdit.setText("console.getConsole()");
            consoleEdit.setText(console.getConsole());
        }
    }
}
