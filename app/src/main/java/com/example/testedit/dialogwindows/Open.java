package com.example.testedit.dialogwindows;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.testedit.MainInterface;
import com.example.testedit.R;
import com.example.testedit.date.FData;
import com.example.testedit.search.InformationSearch;
import com.example.testedit.search.Search;
import com.example.testedit.search.SearchAdapter;

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
    FData fData = new FData();
    String ONION_DIR = fData.FEB_ONION_DIR;
    String DIR = fData.DIR;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Open(Activity context, String directory) {
        this.directory = directory;
        this.context = context;
        mainInterface = (MainInterface) context;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final View linearlayout = context.getLayoutInflater().inflate(R.layout.dialog_open, null);
        dialog.setView(linearlayout);
        alertDialog = dialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.create();
        listView = linearlayout.findViewById(R.id.OpenListView);
        listView.setOnItemClickListener(this);
        sendFile = linearlayout.findViewById(R.id.SendFile);
        delete = linearlayout.findViewById(R.id.deleteFile);
        back = linearlayout.findViewById(R.id.back);
        cancel = linearlayout.findViewById(R.id.cancel);
        sendFile.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        cancel.setOnClickListener(v -> {
            /**Закрываем Диалог*/
            alertDialog.cancel();
        });
        sendFile.setOnClickListener(v -> {
            /**Отпровляем фаилы и скрываем кнопки*/
            sendFile();
            sendFile.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            alertDialog.cancel();
        });
        delete.setOnClickListener(v -> {
            delete();
            sendFile.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);

        });
        back.setOnClickListener(v -> back());
        showDirectory(directory);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final CheckBox checkBox = view.findViewById(R.id.check);
            /**разблокировка checkBox*/
            checkBox.setVisibility(view.VISIBLE);
            if (searchAdapter.getItem(position).getCheck() == false) {
                checkBox.setChecked(true);
                searchAdapter.getItem(position).setCheck(true);
                /**При выделении проверяем на состояния checkBox ListView
                 * если оно ложное меняем его состояние на истинное
                 * потом меняем состояние кнопок*/
                sendFile.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            } else {
                checkBox.setChecked(false);
                searchAdapter.getItem(position).setCheck(false);
                sendFile.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
            return true;
        });
        dialog.create();
    }

    /**
     * метот в котором буду выпольнятьса основные операции удаление
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void delete() {
        int k = arrayList.size();
        int s = 0;
        while (s != k) {
            if (arrayList.get(s).getCheck() == true) {
                fData.deleteDir(directory + arrayList.get(s).getNumber());
            }
            s++;
        }
        showDirectory(directory);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void back() {
        sendFile.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        String newDirectory = "";
        String[] parts = directory.split("/");
        for (int i = 0; i != parts.length - 1; i++) {
            if (i == 0) {
                newDirectory = newDirectory + parts[i];
            } else {
                newDirectory = newDirectory + "/" + parts[i];
            }
        }

        if (newDirectory.equals(DIR)) {
            showDirectory(DIR + "/python/");
        } else {
            showDirectory(newDirectory + "/");
        }
    }

    /**
     * Проводник
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDirectory(String analogDir) {
        directory = analogDir;
        arrayList.clear();
        final String[] sDirList = new FData().arrayDir(analogDir);
        for (int i = 0; i < sDirList.length; i++) {
            String file = analogDir + sDirList[i];
            String time = new FData().getTime(file);
            if (new FData().checkFile(file)) {
                if (sDirList[i].endsWith(".py")) {
                    String about = new FData().aboutFile(file);
                    arrayList.add(new Search(sDirList[i], time, about, R.drawable.ic_filepython, false));
                } else {
                }
            } else {
                String[] files = new FData().arrayDir(file);
                String about = new InformationSearch().infoProject(files);
                if (sDirList[i].endsWith("project")) {
                    arrayList.add(new Search(sDirList[i], time, about, R.drawable.ic_python_prog, false));
                } else {
                    arrayList.add(new Search(sDirList[i], time, about, R.drawable.ic_folder_python_3, false));
                }
            }
        }
        searchAdapter = new SearchAdapter(context, R.layout.iteam_row, arrayList);
        listView.setAdapter(searchAdapter);
    }

    /**
     * Метод с помошью которого мы отпровляем фаилы на другое приложение
     */

    public void sendFile() {
        ArrayList<Uri> uriList = new ArrayList();
        int size = fData.arrayFile(directory).length;
        for (int i = 0; i < size; i++) {
            try {
                if (searchAdapter.getItem(i).getCheck()) {
                    uriList.add(fData.getUriForFile(context, directory + arrayList.get(i).getNumber()));
                }
            } catch (Exception e) {

            }
        }
        fData.sendFile(context, uriList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (new FData().checkFile(directory + searchAdapter.getItem(position).getNumber())) {
            mainInterface.setEditText(new FData().readFile(directory + searchAdapter.getItem(position).getNumber()));
            mainInterface.setFileName(searchAdapter.getItem(position).getNumber());
            mainInterface.setDIRECTORY(directory);
            alertDialog.cancel();
        } else {
            directory = directory + searchAdapter.getItem(position).getNumber();
            // project_Name = mAdapter.getItem(position).getNomber();
            showDirectory(directory + "/");
        }
    }
}
