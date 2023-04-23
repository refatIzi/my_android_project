package com.example.testedit.date;

import com.example.testedit.connect.Connect;
import com.example.testedit.setting.Setting;

public class DataSetting {

    private Connect connect;
    private Setting setting;

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

    public static Setting_use newSetting() {
        return new DataSetting().new Setting_use();
    }

    public class Setting_use {
        Setting_use() {
        }

        public Setting_use setConnect(Connect connect) {
            DataSetting.this.connect = connect;
            return this;
        }

        public Setting_use setSetting(Setting setting) {
            DataSetting.this.setting = setting;
            return this;
        }

        public DataSetting accept() {
            return DataSetting.this;
        }

    }

}
