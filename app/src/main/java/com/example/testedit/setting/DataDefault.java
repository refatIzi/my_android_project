package com.example.testedit.setting;

import com.example.testedit.connect.Connect;
import com.example.testedit.connect.Protocol;

public enum DataDefault {
    DEFAULT{
    };
    public Connect connect= Connect
            .newHost()
            .ipAddress("192.168.1.1")
            .setUserName("user")
            .setPassword("password")
            .setPort("22")
            .setProtocol(Protocol.SSH)
            .setProjectDirHost("/")
            .setPython(Connect.Python.python3)
            .accept();
    public Setting setting = Setting
            .newSet()
            .setTextSize(16)
            .accept();
}
