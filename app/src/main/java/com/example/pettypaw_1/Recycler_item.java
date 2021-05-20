package com.example.pettypaw_1;

import android.graphics.drawable.Drawable;

public class Recycler_item {

    private String PetName ;
    private String Detail ;
    private boolean isSelected_feed;
    private boolean isSelected_walk;
    private Drawable iconDrawable;

    public void setPetName(String petName) {
        PetName = petName ;
    }
    public void setDetail(String detail) {
        Detail = detail ;
    }
    public void setSelected_feed(boolean selected)
    {
        isSelected_feed = selected;
    }
    public void setSelected_walk(boolean selected)
    {
        isSelected_walk = selected;
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
    public boolean getSelected_feed()
    {
        return isSelected_feed;
    }
    public boolean getSelected_walk()
    {
        return isSelected_walk;
    }


    public Drawable getIcon(){
        return this.iconDrawable;
    }
}