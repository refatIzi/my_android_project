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
import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.setting.Data;
import com.example.testedit.setting.DataSetting;
import com.example.testedit.search.SearchAdapter;
import com.example.testedit.search.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Open implements AdapterView.OnItemClickListener {
    AlertDialog alertDialog;
    Context context;
    MainInterface mainInterface;
    private List<Search> arrayList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private ListView listView;
    String directory;
    ImageButton sendFile;
    ImageButton delete;
    ImageButton back;
    ImageButton cancel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Open(Activity context, String directory) {
        this.directory = directory;
        this.context = context;
        mainInterface = (MainInterface) context;

        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_open, null);
        ratingdialog.setView(linearlayout);
        alertDialog = ratingdialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ratingdialog.create();
        listView = linearlayout.findViewById(R.id.OpenListView);
        listView.setOnItemClickListener(this);
        sendFile = linearlayout.findViewById(R.id.SendFile);
        delete = linearlayout.findViewById(R.id.deleteFile);
        back = linearlayout.findViewById(R.id.back);
        cancel = linearlayout.findViewById(R.id.cancel);
        sendFile.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Закрываем Диалог*/
                alertDialog.cancel();
            }
        });
        sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Отпровляем фаилы и скрываем кнопки*/
                Send();
                sendFile.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                alertDialog.cancel();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Delete();
                sendFile.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        showDirectory(directory);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CheckBox checkBox = view.findViewById(R.id.check);
                /**разблокировка checkBox*/
                checkBox.setVisibility(view.VISIBLE);
                if (searchAdapter.getItem(position).getChecket() == false) {
                    checkBox.setChecked(true);
                    searchAdapter.getItem(position).setChecket(true);
                    /**При выделении проверяем на состояния checkBox ListView
                     * если оно ложное меняем его состояние на истинное
                     * потом меняем состояние кнопок*/
                    sendFile.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    checkBox.setChecked(false);
                    searchAdapter.getItem(position).setChecket(false);
                    sendFile.setVisibility(View.GONE);
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
        int k = arrayList.size();
        int s = 0;
        while (s != k) {
            if (arrayList.get(s).getChecket() == true) {
                new Data().deleteDir(directory + arrayList.get(s).getNomber());
            }
            s++;
        }
        showDirectory(directory);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Back() {
        sendFile.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        String new_Directory = "";
        String[] parts = directory.split("/");
        for (int i = 0; i != parts.length - 1; i++) {
            if (i == 0) {
                new_Directory = new_Directory + parts[i];
            } else {
                new_Directory = new_Directory + "/" + parts[i];
            }
        }

        if (new_Directory.equals(Environment.getExternalStorageDirectory().toString())) {
            new_Directory = Environment.getExternalStorageDirectory().toString() + "/python";
            showDirectory(new_Directory + "/");
        } else {
            showDirectory(new_Directory + "/");
        }
        directory = new_Directory + "/";
    }

    /**
     * Проводник для погказа сохраненных файлов в папке
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDirectory(String aDirectory) {
        arrayList.clear();
        final String[] sDirList = new Data().arrayDir(aDirectory);
        for (int i = 0; i < sDirList.length; i++) {
            String file = aDirectory + sDirList[i];
            String time = new Data().getTime(file);
            if (new Data().checkFile(file)) {
                if (sDirList[i].endsWith(".py")) {
                    arrayList.add(new Search(sDirList[i], time,
                            DataSetting.readInformation(sDirList[i],
                                    "information", aDirectory), R.drawable.ic_filepython, false));
                } else {
                }
            } else {
                if (sDirList[i].endsWith("project"))
                    arrayList.add(new Search(sDirList[i], time,
                            DataSetting.readInformation(sDirList[i],
                                    "information", aDirectory), R.drawable.ic_python_prog, false));
                else
                    arrayList.add(new Search(sDirList[i], time,
                            DataSetting.readInformation(sDirList[i],
                                    "information", aDirectory), R.drawable.ic_folder_python_3, false));
            }
        }
        searchAdapter = new SearchAdapter(context, R.layout.iteam_row, arrayList);
        listView.setAdapter(searchAdapter);
    }

    /**
     * Метод с помошью которого мы отпровляем фаилы на другое приложение
     */

    public void Send() {

        ArrayList<Uri> uris = new ArrayList();
        Intent intent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        int size = new Data().arrayFile(directory).length;

        for (int i = 0; i != size; i++) {
            if (i != size) {
                try {
                    if (searchAdapter.getItem(i).getChecket() == true) {
                        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, new File(directory + arrayList.get(i).getNomber()));
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
        if (new Data().checkFile(directory + searchAdapter.getItem(position).getNomber())) {
            mainInterface.setEditText(new Data().readFile(directory + searchAdapter.getItem(position).getNomber()));
            mainInterface.setFileName(searchAdapter.getItem(position).getNomber());
            mainInterface.setDirectory(directory);
            alertDialog.cancel();
        } else {
            directory = directory + searchAdapter.getItem(position).getNomber() + "/";
            // project_Name = mAdapter.getItem(position).getNomber();
            showDirectory(directory + "/");
        }
    }
}
