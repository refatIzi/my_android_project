package com.example.testedit.setting;

import android.os.Environment;

import com.example.testedit.connect.Connect;
import com.example.testedit.connect.Protocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Data {


    private final String DIR = Environment.getExternalStorageDirectory().toString() + "/";
    private final String FEB_ONION_DIR = Environment.getExternalStorageDirectory().toString() + "/python/";


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
        BufferedReader br = null;
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
                .newSaving()
                .setConnect(connect())
                .setSetting(setting())
                .accept();
    }

}
