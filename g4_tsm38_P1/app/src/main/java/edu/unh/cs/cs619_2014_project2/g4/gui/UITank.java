package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.graphics.Canvas;

import edu.unh.cs.cs619_2014_project2.g4.R;
import edu.unh.cs.cs619_2014_project2.g4.game.Tank;

/**
 * Created by Tay on 11/8/2014.
 */


public class UITank extends UIEntity {
    private Tank m_tank;

    public UITank(Tank tank) {
        m_tank = tank;
    }


    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getImage() {

        if (m_tank.getPlayerIsEnemy())
        {
            if (m_tank.getDirection() == Tank.Direction.DOWN)
                return R.drawable.enemytankdown;
            else if (m_tank.getDirection() == Tank.Direction.UP)
                return R.drawable.enemytankup;
            else if (m_tank.getDirection() == Tank.Direction.LEFT)
                return R.drawable.enemytankleft;
            else if (m_tank.getDirection() == Tank.Direction.RIGHT)
                return R.drawable.enemytankright;
        }
        else
        {
            if (m_tank.getDirection() == Tank.Direction.DOWN)
                return R.drawable.playertankdown;
            else if (m_tank.getDirection() == Tank.Direction.UP)
                return R.drawable.playertankup;
            else if (m_tank.getDirection() == Tank.Direction.LEFT)
                return R.drawable.playertankleft;
            else if (m_tank.getDirection() == Tank.Direction.RIGHT)
                return R.drawable.playertankright;
        }


        return R.drawable.kangoo;

    }

    @Override
      public String toString()
    {

        return "T";
    }

}
