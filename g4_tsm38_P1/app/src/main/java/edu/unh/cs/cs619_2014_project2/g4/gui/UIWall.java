package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.graphics.Canvas;

import edu.unh.cs.cs619_2014_project2.g4.R;
import edu.unh.cs.cs619_2014_project2.g4.game.IWall;
import edu.unh.cs.cs619_2014_project2.g4.game.Wall;

/**
 * Created by Tay on 11/8/2014.
 */
public class UIWall extends UIEntity {
    private Wall wall=null;
    private IWall iWall=null;

    public UIWall(Wall wall) {
        this.wall = wall;
    }
    public UIWall(IWall iwall) {
        this.iWall = iWall;
    }


    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getImage() {
        if(wall==null)
            return R.drawable.indestructiblewall;
        else
            return R.drawable.destructiblewall;
    }
    @Override
    public String toString() {
        if(wall==null)
            return "I";
        else
            return "W";
    }

}

