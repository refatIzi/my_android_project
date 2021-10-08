package com.example.testedit;

import android.os.Build;
import android.widget.Adapter;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Work_with_File {
    /**
     * Информация о последнем изменении фаила
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static FileTime getTime(File file) {
        BasicFileAttributes attr = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Path fl = file.toPath();
            try {
                attr = Files.readAttributes(fl, BasicFileAttributes.class);
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
        FileOutputStream fos = null;
        File directory = new File(dir);
        File filename = new File(directory, fileName);
        try {

            fos = new FileOutputStream(filename);
            if (text.length() > 0) {
                fos.write(text.getBytes());
            } else {

            }
            //  Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {

            //  Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
public static void send(Adapter adapter,String dir)
{

}

}
