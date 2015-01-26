package edu.unh.cs.cs619_2014_project2.g4.game;

/**
 * Class factory that defines entities
 * @author Group 4
 */


public class EntityFactory {
    int grid[][];
    long myTank;

    /**
     * Class' constructor
     * @param grid - 2D array that has elements to represent an entity
     * @param tankid - Tank ID
     */
    public EntityFactory(int grid[][], long tankid){
    this.grid = grid;
        this.myTank = tankid;

    }

    /**
     * @return array of entities
     */
    public Entity[] createEntities() {

       Entity[] entity = new Entity[257];

int count = 0;
        String parseString;
        for (int x = 0; x < 16; x++){
            for(int y = 0; y < 16; y++){
                parseString = Integer.toString(grid[x][y]);
                if(grid[x][y] == 1000){
                    entity[count] = new IWall(x,y);
                }else if(grid[x][y] > 1000 && grid[x][y] < 2000){
                    entity[count] = new Wall(x,y, grid[x][y]-1000);
                }else if(grid[x][y] > 2000000 && grid[x][y] < 3000000){
                    entity[count] = new Bullet(
                            x,
                            y,
                            Integer.parseInt(parseString.substring(4,7)),
                            Integer.parseInt(parseString.substring(1,4)));
                }else if(grid[x][y] > 10000000 && grid[x][y] < 20000000) {
                    entity[count] = new Tank(
                            x,                                                              //xPos
                            y,                                                              //yPos
                            Integer.parseInt(parseString.substring(4,7)),                   //_h
                            Integer.parseInt(parseString.substring(1,4)),                   //id
                            Integer.parseInt(parseString.substring(7,8)),                   //dir
                            (int)myTank != Integer.parseInt(parseString.substring(1,4)));    //isEnemy
                } else if(grid[x][y] == 0){
                        entity[count] = new Blank(x,y);
                }
                else
                    entity[count] = new Blank(x,y);


count++;
            }
        }

        return entity;
    }

    /**
     * Method to print representation of entity factory
     * @return representation of entity factory
     */
    @Override
    public String toString()
    {
        return "EF";
    }
}