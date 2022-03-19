package com.example.testedit.helpinfo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testedit.MainActivity;
import com.example.testedit.MainInterface;
import com.example.testedit.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Help extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    String[] functions1={"self","def","as","assert","break","continue","del","elif","else","except","finally","for","from","global","if","import","in","pass","raise","return","try","while","with","yield"};
    String[] functions2={"min()","setattr()","abs()","all()","dir()","hex()","next()","any()","divmod()","id()","sorted()","ascii()","enumerate()","input()","oct()","max()","round()",
            "bin()","eval()","exec()","isinstance()","ord()","sum()","filter()","issubclass()","pow()","iter()","print()","callable()","format()","delattr()",
            "len()","chr()","range()","vars()","getattr()","locals()","repr()","zip()","compile()","globals()","map()","reversed()","__import__()","hasattr()","hash()","memoryview()"};


    ImageButton tab, sc_1, sc_2, divide, procent, hashtag, plas, minus, equals, down_left, down_right;
    LinearLayout linearMessage;
    ListView listView;
    private List<HelpInfo> helper = new ArrayList<>();
    HelpAdapter adapter;
    MainInterface mainInterface;
    Context context;
    String help;
    EditText configuration;

    @SuppressLint("ValidFragment")
    public Help(MainActivity mainActivity) {
        mainInterface = (MainInterface) mainActivity;
        context = mainActivity;

    }

    public void helpAdd(String help) {
        this.help=help;
        for (int i = 0; i < functions1.length; i++) {
            if (functions1[i].startsWith(help))
                helper.add(new HelpInfo(functions1[i], "no info"));
        }
    }

    public void clear() {
        adapter.clear();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public String getConfiguration()
    {
        String Conf="";
        if(!configuration.getText().toString().isEmpty())
            Conf=configuration.getText().toString();
                    else Conf=" ";
        return Conf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);
        listView = v.findViewById(R.id.search);
        // adapter = new ArrayAdapter<>(context,      R.layout.list_help, helper);
        // Привяжем массив через адаптер к ListView
        //    listView.setAdapter(adapter);
        adapter = new HelpAdapter(context, R.layout.iteam_help, helper);

        listView.setAdapter(adapter);

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
        linearMessage = v.findViewById(R.id.linerMessage);
        configuration=v.findViewById(R.id.configuration);

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
        linearMessage.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
              //  mainInterface.Show(adapter.getItem(position).getInfirmation());
                mainInterface.Information(adapter.getItem(position).getHelp());
                return true;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

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
            case R.id.linerMessage:
                Toast.makeText(context, "Liner Button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //  mainInterface.Show(adapter.getItem(position).getHelp().replaceAll(help,""));
        mainInterface.setEdit(adapter.getItem(position).getHelp().replaceAll(help,""));
    }
}