package edu.unh.cs.cs619_2014_project2.g4.game;

/**
 * Class that defines a wall
 * @author Group 4
 */
public class Wall extends Health{

    /**
     * Class' constructor
     * @param x - x position of entity in the grid
     * @param y - y position of entity in the grid
     * @param health - entity's health
     */
    public Wall(int x, int y, int health){
        super(x, y, health);
    }

    /**
     * Method to print entity in the grid
     * @return representation of entity
     */
    @Override
    public String toString()
    {
        return "W";
    }


}
