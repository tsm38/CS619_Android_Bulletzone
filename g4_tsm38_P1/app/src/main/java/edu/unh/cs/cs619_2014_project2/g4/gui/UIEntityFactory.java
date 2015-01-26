package edu.unh.cs.cs619_2014_project2.g4.gui;

/**
 * Created by Tay on 11/8/2014.
 */

import edu.unh.cs.cs619_2014_project2.g4.game.Blank;
import edu.unh.cs.cs619_2014_project2.g4.game.Bullet;
import edu.unh.cs.cs619_2014_project2.g4.game.Entity;
import edu.unh.cs.cs619_2014_project2.g4.game.IWall;
import edu.unh.cs.cs619_2014_project2.g4.game.Tank;
import edu.unh.cs.cs619_2014_project2.g4.game.Wall;

public class UIEntityFactory {

    public UIEntityFactory(){
    }

    public UIEntity[] createUIEntities(Entity[] entities) {
        UIEntity[] uiEntities = new UIEntity[257];

        for (int i = 0; i < entities.length; i++) {
           Entity c = entities[i];
            if (c instanceof Wall)
                uiEntities[i] = new UIWall((Wall) entities[i]);
            else if (c instanceof Bullet)
                uiEntities[i] = new UIBullet((Bullet) entities[i]);
            else if (c instanceof IWall)
                uiEntities[i] = new UIWall((IWall) entities[i]);
            else if (c instanceof Tank)
                uiEntities[i] = new UITank((Tank) entities[i]);
            else if ( c instanceof Blank)
                uiEntities[i] = new UIBlank((Blank) entities[i]);
            else
                uiEntities[i] = new UIBlank((Blank) entities[i]);




        }
        return uiEntities;
    }
}