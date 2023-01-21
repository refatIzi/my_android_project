package com.example.testedit.setting;

import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.example.testedit.connect.Connect;
import com.example.testedit.connect.Protocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class Data {


    private final String DIR = Environment.getExternalStorageDirectory().toString() + "/";
    public final String FEB_ONION_DIR = Environment.getExternalStorageDirectory().toString() + "/python/";


    public void writeData(DataSetting dataSetting) {
        try {
            File filename = new File(FEB_ONION_DIR, "onion.json");
            JSONObject object = new JSONObject();

            object.put("IpAddress", dataSetting.getConnect().getIpAddress());
            object.put("UserName", dataSetting.getConnect().getUserName());
            object.put("Password", dataSetting.getConnect().getPassword());
            object.put("Port", dataSetting.getConnect().getPort());
            object.put("ProjectDirHost", dataSetting.getConnect().getProjectDirHost());
            object.put("Protocol", dataSetting.getConnect().getProtocol());
            object.put("Python", dataSetting.getConnect().getPython());
            object.put("TextSize", dataSetting.getSetting().getTextSize());

            new FileOutputStream(filename).write(object.toString().getBytes());
        } catch (IOException ex) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {

        File filename = new File(FEB_ONION_DIR, "onion.json");
        String JSON = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = br.readLine()) != null) {
                JSON = JSON + str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON;
    }

    private String ipAddress;
    private String userName;
    private String password;
    private String port;
    private Protocol protocol;
    private String projectDirHost;
    private Connect.Python python;

    public Connect connect() {

        try {
            JSONObject jsonRootObject = new JSONObject(toString());
            ipAddress = jsonRootObject.getString("IpAddress");
            userName = jsonRootObject.getString("UserName");
            password = jsonRootObject.getString("Password");
            port = jsonRootObject.getString("Port");
            protocol = Protocol.valueOf(jsonRootObject.getString("Protocol"));
            projectDirHost = jsonRootObject.getString("ProjectDirHost");
            python = Connect.Python.valueOf(jsonRootObject.getString("Python"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Connect
                .newHost()
                .ipAddress(ipAddress)
                .setUserName(userName)
                .setPassword(password)
                .setPort(port)
                .setProtocol(protocol)
                .setProjectDirHost(projectDirHost)
                .setPython(python)
                .accept();
    }

    private int textSize;

    public Setting setting() {
        try {
            JSONObject jsonRootObject = new JSONObject(toString());
            textSize = Integer.parseInt(jsonRootObject.getString("TextSize"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Setting
                .newSet()
                .setTextSize(textSize)
                .accept();
    }

    public DataSetting readData() {
        return DataSetting
                .newSetting()
                .setConnect(connect())
                .setSetting(setting())
                .accept();
    }

    /**
     * Here we check file if is not directory we create directory
     */
    public void checkDirectory(String directory) {
        File theDir = new File(directory);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    /**
     * Here we delete file
     */
    public void deleteFile(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }
        file.delete();
    }

    /**
     * Here we delete directory
     */
    public void deleteDir(String directory) {
        File file = new File(directory);
        if (file.isFile()) {
            file.delete();
        } else {
            deleteFile(file);
        }
    }

    public boolean checkFile(String file) {
        return new File(file).isFile();
    }

    public String readFile(String FileName) {
        String code = "";
        /** формируем объект File, который содержит путь к файлу*/
        File sdFile = new File(FileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String string;
            while ((string = br.readLine()) != null) {
                code = code + string + "\n";
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    public File[] arrayFile(String directory) {
        File listFile = new File(directory);
        return listFile.listFiles();
    }

    public String[] arrayDir(String directory) {
        File dir = new File(directory);
        return dir.list();
    }
    /**
     * Информация о последнем изменении фаила
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String getTime(String directory) {
        BasicFileAttributes attr = null;
        File file = new File(directory);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Path path = file.toPath();
            try {
                attr = Files.readAttributes(path, BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return attr.lastModifiedTime().toString().replaceAll("T|T1|T2|Z", "\n");
        }
        return "";
    }
}
