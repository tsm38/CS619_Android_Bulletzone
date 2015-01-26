package edu.unh.cs.cs619_2014_project2.g4.game;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import edu.unh.cs.cs619_2014_project2.g4.server.BulletZoneRestClient;

/**
 * Created by Tay on 11/29/2014.
 */
@EBean
public class ConstraintHandler {
    private static final String LOG_TAG = "ConstraintHandler";

    public  boolean xMove = true;
    public  boolean yFire = true;
    public  boolean wTurn = true;






    //tank can only move once every x seconds, .5
    @Background
    public void xMove(long tankId, byte direction, BulletZoneRestClient restClient)
    {
        if (xMove)
        {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // Log.v(LOG_TAG, ".5 seconds");
                            xMove = true;

                        }
                    },
                    500
            );
            restClient.move(tankId, direction);
            xMove = false;

        }

    }

    //tank can only fire once every y seconds, .5
    @Background
    public void yFire(long tankId, BulletZoneRestClient restClient)
    {
        //Log.v(LOG_TAG, "here");
        if (yFire)
        {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            //  Log.v(LOG_TAG, ".5 seconds");
                            yFire = true;

                        }
                    },
                    500
            );

            if(tankId!=-1) {
                yFire = false;
                restClient.fire(tankId);
            }
        }

    }


    @Background
    public void move( Tank myTank, long tankId, byte direction, BulletZoneRestClient restClient) {
        switch(direction)
        {
            case 0: //up
                if (myTank.getDirection() == Tank.Direction.UP ||
                        myTank.getDirection() == Tank.Direction.DOWN)
                    xMove(tankId, (byte) 0, restClient);
                else
                    wTurn(tankId, (byte) 0, restClient);
                break;
            case 2: //right
                if (myTank.getDirection() == Tank.Direction.RIGHT  ||
                        myTank.getDirection() == Tank.Direction.LEFT)
                    xMove(tankId, (byte)2, restClient);
                else
                    wTurn(tankId, (byte) 2, restClient);
                break;
            case 4: //down
                if (myTank.getDirection() == Tank.Direction.DOWN ||
                        myTank.getDirection() == Tank.Direction.UP)
                    xMove(tankId, (byte)4, restClient);
                else
                    wTurn(tankId, (byte) 4, restClient);
                break;
            case 6: //left
                if (myTank.getDirection() == Tank.Direction.LEFT ||
                        myTank.getDirection() == Tank.Direction.RIGHT)
                    xMove(tankId, (byte)6, restClient);
                else
                    wTurn(tankId, (byte) 6, restClient);
                break;
            default:
                break;
        }
    }


    @Background
    public void wTurn(long tankId, byte direction, BulletZoneRestClient restClient) {


        if (wTurn)
        {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Log.v(LOG_TAG, ".5 seconds");
                            wTurn = true;

                        }
                    },
                    500
            );
            restClient.turn(tankId, direction);
            wTurn = false;

        }
    }



    public boolean zBullets(int maxBullets, Entity[] saveEntities, long tankId ) {

        //are there enough bullets
        int bulletsOnField = 0;
        for (Entity entity : saveEntities) {
            if (entity != null && entity.getClass() != Bullet.class)
                continue;

            Bullet b = (Bullet) entity;
            if ((b != null && b.getTank() == tankId))
                bulletsOnField++;
        }
        if (bulletsOnField < maxBullets)
            return true;
        else
            return false;
    }

    @Background
    public void fire(int maxBullets, Entity[] saveEntities, long tankId, BulletZoneRestClient restClient) {

        //are there enough bullets
        if (zBullets(maxBullets, saveEntities, tankId))
            yFire(tankId, restClient);


    }
}
