package edu.unh.cs.cs619_2014_project2.g4.test;

import android.test.AndroidTestCase;

import edu.unh.cs.cs619_2014_project2.g4.ControlActivity;
import edu.unh.cs.cs619_2014_project2.g4.R;
import edu.unh.cs.cs619_2014_project2.g4.game.Blank;
import edu.unh.cs.cs619_2014_project2.g4.game.Bullet;
import edu.unh.cs.cs619_2014_project2.g4.game.ConstraintHandler;
import edu.unh.cs.cs619_2014_project2.g4.game.Entity;
import edu.unh.cs.cs619_2014_project2.g4.game.IWall;
import edu.unh.cs.cs619_2014_project2.g4.game.Tank;
import edu.unh.cs.cs619_2014_project2.g4.game.Wall;
import edu.unh.cs.cs619_2014_project2.g4.gui.UIBullet;
import edu.unh.cs.cs619_2014_project2.g4.gui.UITank;
import edu.unh.cs.cs619_2014_project2.g4.gui.UIWall;


/**
 * Created by Tay on 11/30/2014.
 */
public class TestSimpleFactory extends AndroidTestCase {

    private static final String LOG_TAG = "TestSimpleFactory";
    ControlActivity ca = new ControlActivity();
    int tankId = 127;
    Entity[] saveEntities;
    int[][]  grid;
   // Tank tank = new Tank(1,3,30,127,2, false); //(int xPos, int yPos, int _h, int id, int dir, boolean isEnemy)

    public void testWallX()
    {
        Wall wall = new Wall(50,0,0);
        assertTrue(wall.getX() == 50);
    }

    public void testWallY()
    {
        Wall wall = new Wall(0,50,0);
        assertTrue(wall.getY() == 50);
    }

    public void testWallHealth()
    {
        Wall wall = new Wall(0,0,50);
        assertTrue(wall.getHealth() == 50);
    }

    public void testDestructibleWallImage()
    {
        Wall wall = new Wall(0,0,50);
        assertTrue(wall.getHealth() == 50);

        UIWall uiWall = new UIWall(wall);
        assertTrue(uiWall.getImage() == R.drawable.destructiblewall);


    }


    public void testIndestructibleWallImage()
    {
        IWall wall = new IWall(0,0);


        UIWall uiWall = new UIWall(wall);
        assertTrue(uiWall.getImage() == R.drawable.indestructiblewall);
    }

    //this test shows the ability to iterate through the field and ask about anything
    //including bullets, walls, tankid's, directions, health, damage walls.
    public void testZBulletTrue()
    {
        ConstraintHandler ch = new ConstraintHandler();
        Entity[] myEntities = new Entity[]
                {
                        new Blank(0,0),
                        new Blank(0,0),
                        new Bullet(0,0,0,0),
                        new Tank(0,0,0,127,0,false),
                        new Bullet(0,0,0,0),
                        new Blank(0,0),
                        new Bullet(0,0,0,0)
                };
        assertTrue(ch.zBullets(3,myEntities, 127));

    }




    public void testZBulletFalse()
    {
        ConstraintHandler ch = new ConstraintHandler();
        Entity[] myEntities = new Entity[]
                {
                        new Blank(0,0),
                        new Blank(0,0),
                        new Bullet(0,0,0,127),
                        new Tank(0,0,0,127,0,false),
                        new Bullet(0,0,0,127),
                        new Blank(0,0),
                        new Bullet(0,0,0,127)
                };




        assertFalse(ch.zBullets(3,myEntities, 127));

    }


    public void testBulletX()
    {
        Bullet bullet = new Bullet(50,0,0,0);
        assertTrue(bullet.getX() == 50);
    }

    public void testBulletY()
    {
        Bullet bullet = new Bullet(0,50,0,0);
        assertTrue(bullet.getY() == 50);
    }

    public void testBulletDamage()
    {
        Bullet bullet = new Bullet(0,0,50,0);
        assertTrue(bullet.getDamage() == 50);
    }

    public void testBulletTankID()
    {
        Bullet bullet = new Bullet(0,0,0,50);
        assertTrue(bullet.getTank() == 50);
    }

    public void testBulletImage()
    {
        Bullet bullet = new Bullet(0,0,0,50);
        assertTrue(bullet.getTank() == 50);

        UIBullet uiBullet = new UIBullet(bullet);
        assertTrue(uiBullet.getImage() == R.drawable.bullet);
    }



   public void testTankX()
   {
       Tank tank = new Tank(50,0,0,0,0, false);
       assertTrue(tank.getX() == 50);
   }

    public void testTankY()
    {
        Tank tank = new Tank(0,50,0,0,0, false);
        assertTrue(tank.getY() == 50);
    }

   public void testTankHealth()
   {
       Tank tank = new Tank(0,0,100,0,0, false);
       assertTrue(tank.getHealth() == 100);
   }


    public void testTankId()
    {
        Tank tank = new Tank(0,0,0,5,0, false);
       assertTrue(tank.getID() == 5);




    }

    public void testTankDirectionUp()
    {
        Tank tank = new Tank(0,0,0,0,0, false);
        assertTrue(tank.getDirection() == Tank.Direction.UP);

        UITank uiTank = new UITank(tank);
        assertTrue(uiTank.getImage() == R.drawable.playertankup);
    }

    public void testTankDirectionRight()
    {
        Tank tank = new Tank(0,0,0,0,2, false);
        assertTrue(tank.getDirection() == Tank.Direction.RIGHT);

        UITank uiTank = new UITank(tank);
        assertTrue(uiTank.getImage() == R.drawable.playertankright);
    }

    public void testTankDirectionDown()
    {
        Tank tank = new Tank(0,0,0,0,4, false);
        assertTrue(tank.getDirection() == Tank.Direction.DOWN);

        UITank uiTank = new UITank(tank);
        assertTrue(uiTank.getImage() == R.drawable.playertankdown);
    }

    public void testTankDirectionLeft()
    {
        Tank tank = new Tank(0,0,0,0,6, false);
        assertTrue(tank.getDirection() == Tank.Direction.LEFT);
        UITank uiTank = new UITank(tank);
        assertTrue(uiTank.getImage() == R.drawable.playertankleft);
    }

    public void testTankIsNotEnemy()
    {
        Tank tank = new Tank(0,0,0,0,0, false);
        assertTrue(tank.getPlayerIsEnemy() == false);

        UITank uiTank = new UITank(tank);
        assertTrue(uiTank.getImage() == R.drawable.playertankup);
    }

    public void testTankIsEnemy()
    {
        Tank tank = new Tank(0,0,0,0,0, true);
        assertTrue(tank.getPlayerIsEnemy() == true);

        UITank uiTank = new UITank(tank);
        assertTrue(uiTank.getImage() == R.drawable.enemytankup);

    }

}
