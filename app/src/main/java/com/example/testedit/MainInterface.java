package com.example.testedit;

public interface MainInterface {
    void setEdit(String text);

    void setNumberCode(int progress);

    void setTextSize(int progress);

    void setFileName(String FileName);

    void setEditText(String text);

    void setDirectory(String Directory);

    void setTerminal(String message);

    void setReseult(String message);
    void Back();
    void Forward();
    void Show(String text);
    void Information(String txt);

}
