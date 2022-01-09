package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.example.testedit.BuildConfig;
import com.example.testedit.ListAdapter;
import com.example.testedit.MainInterface;
import com.example.testedit.Model;
import com.example.testedit.R;
import com.example.testedit.Work_with_File;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Open implements AdapterView.OnItemClickListener {
    AlertDialog alertDialog;
    Context context;
    MainInterface mainInterface;
    private List<Model> mLista = new ArrayList<>();
    private ListAdapter mAdapter;
    private ListView mListView;
    String Directory;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Open(Activity context,String Directory)
    {
        this.Directory=Directory;
        this.context=context;
        mainInterface=(MainInterface) context;

        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_open, null);
        ratingdialog.setView(linearlayout);
        alertDialog = ratingdialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ratingdialog.create();
        mListView = linearlayout.findViewById(R.id.OpenListView);
        mListView.setOnItemClickListener(this);
        ImageButton SendFile = linearlayout.findViewById(R.id.SendFile);
        ImageButton delete = linearlayout.findViewById(R.id.deleteFile);
        ImageButton Back = linearlayout.findViewById(R.id.back);
        ImageButton cancel = linearlayout.findViewById(R.id.cancel);
        SendFile.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Закрываем Диалог*/
                alertDialog.cancel();
            }
        });
        SendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Отпровляем фаилы и скрываем кнопки*/
                Send();
                SendFile.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                alertDialog.cancel();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Delete();
                SendFile.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        ShowDirectory(Directory);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CheckBox checkBox = view.findViewById(R.id.check);
                checkBox.setVisibility(view.VISIBLE); //разблокировка checkBox
                if (mAdapter.getItem(position).getChecket() == false) {
                    checkBox.setChecked(true);
                    mAdapter.getItem(position).setChecket(true);
                    /**При выделении проверяем на состояния checkBox ListView
                     * если оно ложное меняем его состояние на истинное
                     * потом меняем состояние кнопок*/
                    SendFile.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    checkBox.setChecked(false);
                    mAdapter.getItem(position).setChecket(false);
                    SendFile.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
                return true;
            }
        });
        ratingdialog.create();
    }
    /**
     * метот в котором буду выпольнятьса основные операции удаление
     * метот удаления https://javadevblog.com/kak-udalit-fajl-ili-papku-v-java.html
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Delete() {
        int k = mLista.size();
        int s = 0;
        File file;
        while (s != k) {

            if (mLista.get(s).getChecket() == true) {

                file = new File(Directory + mLista.get(s).getNomber());
                if (file.isFile()) {
                    file.delete();
                } else {
                    recursiveDelete(file);
                }
            }
            s++;
        }
        ShowDirectory(Directory);
    }

    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Back() {
        String new_Directory = "";
        String[] parts = Directory.split("/");
        for (int i = 0; i != parts.length - 1; i++) {
            if (i == 0) {
                new_Directory = new_Directory + parts[i];
            } else {
                new_Directory = new_Directory + "/" + parts[i];
            }
        }

        if (new_Directory.equals(Environment.getExternalStorageDirectory().toString())) {
            new_Directory = Environment.getExternalStorageDirectory().toString() + "/python";
            ShowDirectory(new_Directory + "/");
        } else {
            ShowDirectory(new_Directory + "/");
        }
        Directory = new_Directory + "/";
    }

    /**
     * Проводник для погказа сохраненных файлов в папке
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ShowDirectory(String aDirectory) {
        mLista.clear();
        File dir = new File(aDirectory);

        final String[] sDirList = dir.list();
        for (int i = 0; i < sDirList.length; i++) {

            File f1 = new File(aDirectory + File.separator + sDirList[i]);
            if (f1.isFile()) {
                if (sDirList[i].endsWith(".py")) {
                    mLista.add(new Model(sDirList[i], Work_with_File.getTime(f1).toString().replaceAll("T1|T2|Z", "\n") + "", Work_with_File.readInformation(sDirList[i], "information", aDirectory), R.drawable.ic_filepython, false));
                } else {
                }
            } else {
                if (sDirList[i].endsWith("project"))
                    mLista.add(new Model(sDirList[i], Work_with_File.getTime(f1).toString().replaceAll("T1|T2|Z", "\n") + "", Work_with_File.readInformation(sDirList[i], "information", aDirectory), R.drawable.ic_python_prog, false));
                else
                    mLista.add(new Model(sDirList[i], Work_with_File.getTime(f1).toString().replaceAll("T1|T2|Z", "\n") + "", Work_with_File.readInformation(sDirList[i], "information", aDirectory), R.drawable.ic_folder_python_3, false));
            }


        }
        mAdapter = new ListAdapter(context, R.layout.iteam_row, mLista);
        mListView.setAdapter(mAdapter);
    }
    /**
     * Метод с помошью которого мы отпровляем фаилы на другое приложение
     */

    public void Send() {
        File listFile = new File(Directory);
        File exportFiles[] = listFile.listFiles();
        /** с помошью єтих методов мы можем узнать сколько обьектов в папке
         //----------------------------------------------------------------
         узнать как подробнее об передачи фаилов на другое приложение прочитайте это
         Сыылка https://qna.habr.com/q/405453
         */
        ArrayList<Uri> uris = new ArrayList<Uri>();
        Intent intent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        for (int i = 0; i != exportFiles.length; i++) {
            /** в цикле проверяем каждый обьект на Чекбок если он ТРУ то меняем на Фалсе
             самый легкий способ как это узнать мы узнаем размер директори. и применив этот размер как количесто обьектов
             в Листв Виев проверий каждый  чекбок на состояние не применяя ВИЕВ*/
            if (i != exportFiles.length) {
                try {
                    if (mAdapter.getItem(i).getChecket() == true) {
                        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, new File(Directory + mLista.get(i).getNomber()));
                        uris.add(uri);
                    }
                } catch (Exception e) {

                }
            }

        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(intent, "Share Image:"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final File aDirectory = new File(Directory + mAdapter.getItem(position).getNomber());
        if (aDirectory.isFile()) {
            mainInterface.setEditText(Work_with_File.readInformation(mAdapter.getItem(position).getNomber(), "readCode", Directory));
            mainInterface.setFileName(mAdapter.getItem(position).getNomber());
            mainInterface.setDirectory(Directory);
            alertDialog.cancel();
        } else {
            Directory = Directory + mAdapter.getItem(position).getNomber() + "/";
            // project_Name = mAdapter.getItem(position).getNomber();
            ShowDirectory(Directory + "/");
        }
    }
}
