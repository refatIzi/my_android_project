package com.example.testedit.dialogwindows.helpinfo;

public class HelpInfo {
    String help;
    String infirmation;
    public HelpInfo(String help, String infirmation)
    {
        this.help=help;
        this.infirmation=infirmation;
    }
    public String getHelp()
    {
        return help;
    }
    public void setHelp(String help)
    {
        this.help=help;
    }
    public String getInfirmation()
    {
        return infirmation;
    }
    public void setInfirmation(String infirmation)
    {
        this.infirmation=infirmation;
    }
}
