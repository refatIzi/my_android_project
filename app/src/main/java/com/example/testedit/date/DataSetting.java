package com.example.testedit.date;

import com.example.testedit.connect.Connect;

public class DataSetting {

    private Connect connect;
    private com.example.testedit.setting.Setting setting;

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public void setSetting(com.example.testedit.setting.Setting setting) {
        this.setting = setting;
    }

    public Connect getConnect() {
        return connect;
    }

    public com.example.testedit.setting.Setting getSetting() {
        return setting;
    }

    public static Setting newSetting() {
        return new DataSetting().new Setting();
    }

    public class Setting {
        Setting() {
        }

        public Setting setConnect(Connect connect) {
            DataSetting.this.connect = connect;
            return this;
        }

        public Setting setSetting(com.example.testedit.setting.Setting setting) {
            DataSetting.this.setting = setting;
            return this;
        }

        public DataSetting accept() {
            return DataSetting.this;
        }

    }

}
