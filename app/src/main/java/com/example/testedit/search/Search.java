package com.example.testedit.search;

public class Search {
    public boolean isChecked;
    public boolean box;
    private String nomber;
    private String edad;
    private String information;
    private  boolean checket;
    private int image;



    public Search(String nomber, String edad, String information, int image, boolean checket){


        this.nomber=nomber;
        this.edad=edad;
        this.information=information;
        this.image=image;
        this.checket=checket;
    }
    public String getNomber(){ return  nomber;}
    public void setNomber(String nomber){this.nomber=nomber;}

    public String getEdad() { return edad;}
    public void setEdad(String edad){
        this.edad=edad;
    }

    public String getInformation(){ return  information;}
    public void setInformation(String information){this.information=information;}

    public int getImage(){return  image;}
    public void  setImage(int image){ this.image=image;}

    public  boolean getChecket(){return checket;}
    public boolean setChecket(boolean checket){
        this.checket=checket;
        return checket;

    }
}
