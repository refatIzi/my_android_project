package com.example.testedit.search;

public class InformationSearch {
    String[] files;

    public InformationSearch() {

    }

    public InformationSearch(String[] files) {

    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String infoProject(String[] files) {
        String information = "";
        for (String file : files) {
            information = information + "[" + file + "] ";
        }

        return information;
    }

}
