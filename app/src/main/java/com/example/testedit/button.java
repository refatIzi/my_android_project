package com.example.testedit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.ImageButton;


@SuppressLint("ValidFragment")
public class button extends Fragment implements View.OnClickListener {

    MainInterface mainInterface;

    public button(MainActivity mainActivity) {
        mainInterface = (MainInterface) mainActivity;
    }


    ImageButton tab, sc_1, sc_2, divide, procent, hashtag, plas, minus, equals, down_left, down_right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_button, container, false);
        // Inflate the layout for this fragment
        tab = v.findViewById(R.id.Table);
        sc_1 = v.findViewById(R.id.sc_1);
        sc_2 = v.findViewById(R.id.sc_2);
        divide = v.findViewById(R.id.divide);
        procent = v.findViewById(R.id.procent);
        hashtag = v.findViewById(R.id.hashtag);
        plas = v.findViewById(R.id.plas);
        minus = v.findViewById(R.id.minus);
        equals = v.findViewById(R.id.equals);
        down_left = v.findViewById(R.id.down_left);
        down_right = v.findViewById(R.id.down_right);

        tab.setOnClickListener(this);
        sc_1.setOnClickListener(this);
        sc_2.setOnClickListener(this);
        divide.setOnClickListener(this);
        procent.setOnClickListener(this);
        hashtag.setOnClickListener(this);
        plas.setOnClickListener(this);
        equals.setOnClickListener(this);
        down_left.setOnClickListener(this);
        down_right.setOnClickListener(this);
        return v;
    }

    /**
     * Кнопки нижней вкладки с помошью которого мы можем добовлять знаки
     */

    public void onClick(View v) {
        switch (v.getId()) {
            //tab,sc_1,sc_2,divide,procent,hashtag,plas,minus,equals,down_left,down_right;
            case R.id.Table:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                //  editText.getText().insert(editText.getSelectionStart(), "    ");
                mainInterface.setEdit("    ");

                break;
            case R.id.sc_1:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("<");
                break;
            case R.id.sc_2:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit(">");
                break;
            case R.id.divide:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("/");
                break;
            case R.id.procent:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("%");
                break;
            case R.id.hashtag:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("#");
                break;
            case R.id.plas:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("+");
                break;
            case R.id.minus:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("-");
                break;
            case R.id.equals:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.setEdit("=");
                break;
            case R.id.down_left:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/

                mainInterface.Back();
                break;
            case R.id.down_right:
                /**метод смомошью которого можно добавлять втекушую позицию текст*/
                mainInterface.Forward();
                break;
        }
    }
}