package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.graphics.Canvas;

import edu.unh.cs.cs619_2014_project2.g4.R;
import edu.unh.cs.cs619_2014_project2.g4.game.Blank;

/**
 * Created by Tay on 11/15/2014.
 */

public class UIBlank extends UIEntity {
    Blank blank;

    public UIBlank(Blank blank) {
        this.blank = blank;
    }

    @Override
    public void draw(Canvas canvas) {

    }


    @Override
    public int getImage() {
        return R.drawable.blank;
    }

    @Override
    public String toString() {
        return "blank";
    }
}
