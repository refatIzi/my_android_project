package com.example.testedit.setting;

public class Setting {
    private String textSize;

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getTextSize() {
        return textSize;
    }

    public static Set newSet(){
        return new Setting().new Set();
    }
    public class Set{
        Set(){

        }
        public Set setTextSize(String textSize) {
            Setting.this.textSize = textSize;
            return this;
        }
        public Setting accept(){
            return Setting.this;
        }
    }
}
