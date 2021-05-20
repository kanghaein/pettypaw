package com.example.pettypaw_1;

import android.graphics.drawable.Drawable;

public class Recycler_item {

    private String PetName ;
    private String Detail ;
    private Drawable iconDrawable;

    public void setPetName(String petName) {
        PetName = petName ;
    }
    public void setDetail(String detail) {
        Detail = detail ;
    }
    public void setIcon(Drawable colorimage){
        iconDrawable = colorimage;
    }

    public String getPetName() {
        return this.PetName ;
    }
    public String getDetail() {
        return this.Detail ;
    }
    public Drawable getIcon(){
        return this.iconDrawable;
    }
}