package com.example.testedit.setting;

import android.os.Build;
import android.os.Environment;
import android.widget.Adapter;

import androidx.annotation.RequiresApi;

import com.example.testedit.connect.Connect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSaving {

    private Connect connect;
    private Setting setting;
    private DataSaving dataSaving;
    private final String PROJECT_DIR = Environment.getExternalStorageDirectory().toString() + "/python/";

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Connect getConnect() {
        return connect;
    }

    public Setting getSetting() {
        return setting;
    }

    public static Saving newSaving() {
        return new DataSaving().new Saving();
    }

    public class Saving {
        Saving() {
        }

        public Saving setConnect(Connect connect) {
            DataSaving.this.connect = connect;
            return this;
        }

        public Saving setSetting(Setting setting) {
            DataSaving.this.setting = setting;
            return this;
        }

        public DataSaving accept() {
            writeData(DataSaving.this);
            return DataSaving.this;
        }
    }

    public void writeData(DataSaving dataSaving) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("feb_onion");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(dataSaving);
            outputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public DataSaving readData() {
        try {
            FileInputStream fileInputStream = new FileInputStream("feb_onion");
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            dataSaving = (DataSaving) inputStream.readObject();
            inputStream.close();
            fileInputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return dataSaving;
    }

    /**
     * Информация о последнем изменении фаила
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static FileTime getTime(File file) {
        BasicFileAttributes attr = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Path path = file.toPath();
            try {
                attr = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return attr.lastModifiedTime();
        }
        return null;
    }

    public static String readInformation(String FileName, String text, String sesion) {
        String infirmation = "", Return = null;

        File Path = new File(sesion);
        /** добавляем свой каталог к пути*/
        Path = new File(Path.getAbsolutePath() + "/");
        /** формируем объект File, который содержит путь к файлу*/
        File sdFile = new File(Path, FileName);
        try {

            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str;
            while ((str = br.readLine()) != null) {
                if (text.equals("readCode")) {
                    infirmation = infirmation + str + "\n";
                } else {
                    infirmation = infirmation + str;
                }
            }
            if (text.length() > 0 && text.equals("information")) {
                Matcher m = Pattern.compile("\\#(([^\\#]+))\\##").matcher(infirmation);
                if (m.find())
                    Return = m.group(1);
                else
                    Return = "There is no information in the code. Probably someone forgot to write it.";
            } else {
                Return = infirmation;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Return;
    }

    /**
     * Метод записи фаила
     */
    public static void saveFile(String fileName, String text, String dir) {
        File directory = new File(dir);
        File filename = new File(directory, fileName);
        try {
            if (text.length() > 0) {
                new FileOutputStream(filename)
                        .write(text.getBytes());
            } else {
            }
            //  Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {

            //  Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void send(Adapter adapter, String dir) {

    }

}
