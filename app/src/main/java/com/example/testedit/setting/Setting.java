package com.example.testedit.setting;

public class Setting {
    private int textSize;

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextSize() {
        return textSize;
    }

    public static Set newSet(){
        return new Setting().new Set();
    }
    public class Set{
        Set(){

        }
        public Set setTextSize(int textSize) {
            Setting.this.textSize = textSize;
            return this;
        }
        public Setting accept(){
            return Setting.this;
        }
    }
    @Override
    public String toString() {
        return "Text Suze "+textSize;
    }
}
