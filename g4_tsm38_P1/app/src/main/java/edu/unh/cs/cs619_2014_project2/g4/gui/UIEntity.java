package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.graphics.Canvas;

/**
 * Created by Tay on 11/8/2014.
 */
public abstract class UIEntity {


    public abstract void draw(Canvas canvas);
    public abstract int getImage();
    @Override public String  toString(){

      return "g";
    }


}
