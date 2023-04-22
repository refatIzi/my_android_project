package com.example.testedit.search;

public class InformationSearch {
    /**тут ми надаємо інформацію стосовно кількості фацлів в папці який зберігається в массиві
     * далі ми можемо повернути данні в форматі String в квадратних дужках за допомогою
     * метода infoProject*/
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
