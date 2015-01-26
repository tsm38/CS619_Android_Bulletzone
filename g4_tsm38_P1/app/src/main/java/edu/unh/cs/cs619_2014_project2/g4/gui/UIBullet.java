package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import edu.unh.cs.cs619_2014_project2.g4.R;
import edu.unh.cs.cs619_2014_project2.g4.game.Bullet;

/**
 * Created by Tay on 11/8/2014.
 */
public class UIBullet extends UIEntity {
Bullet bullet;

    public UIBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect =  new Rect( bullet.getY()+1, bullet.getX()+1 , bullet.getY(), bullet.getX());
        int rad = 1;
        canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(), rad, new Paint());

    }

    @Override
    public int getImage() {
        return R.drawable.bullet;

    }
    @Override
    public String toString() {
        return "B";
    }
}
