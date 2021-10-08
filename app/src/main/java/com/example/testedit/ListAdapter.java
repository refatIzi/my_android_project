package com.example.testedit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Model> {
    private List<Model> mList;
    private Context mContext;
    Model model;
    private int resourseLayoute;
    public ListAdapter (@NonNull Context context, int resourse, List<Model> objects){

        super(context,resourse,objects);
        this.mList=objects;
        this.mContext=context;
        this.resourseLayoute=resourse;

    }
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        View view=convertView;
        if(view==null)
            view= LayoutInflater.from(mContext).inflate(resourseLayoute,null);
           model =mList.get(position);
        ImageView image=view.findViewById(R.id.imageView);
        image.setImageResource(model.getImage());
        TextView textNomber=view.findViewById(R.id.txtNomber);
        textNomber.setText(model.getNomber());
        TextView textEdad=view.findViewById(R.id.txtViewEdad);
        textEdad.setText(model.getEdad());
        TextView infirmation=view.findViewById(R.id.inform);
        infirmation.setText(model.getInformation());

        final CheckBox checkBox=view.findViewById(R.id.check);
        // пока Чек бокс мы не будет задействовать он нам будет нужен попоже
        if(mList.get(position).getChecket()==false) { //закріваем все чекбоксы так как будут мешать для кликабельности

            checkBox.setVisibility(view.INVISIBLE);//block checkBox
            checkBox.setChecked(false);

        }
        else {
            checkBox.setVisibility(view.VISIBLE);//
            checkBox.setChecked(true);
        }

        final View finalView = view;
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //   checked = read.isChecked();
                if(mList.get(position).getChecket()==false) {
                    //   checkBox.setVisibility(view.INVISIBLE);//block checkBox
                    //  checkBox.setChecked(true);
                    mList.get(position).setChecket(true);


                }
                else {
                    //   checkBox.setVisibility(view.VISIBLE);//block checkBox
                    //  checkBox.setChecked(false);

                    mList.get(position).setChecket(false);
                    checkBox.setVisibility(view.INVISIBLE);//block checkBox
                }
            }
        });


        return view;
    }
}
