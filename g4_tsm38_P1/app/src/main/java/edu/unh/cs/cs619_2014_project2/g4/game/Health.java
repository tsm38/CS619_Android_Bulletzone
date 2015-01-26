package edu.unh.cs.cs619_2014_project2.g4.game;

/**
 * Class that defines a tank
 * @author Group 4
 */

public abstract class Health extends Entity {

    int health;

    /**
     * Class' constructor
     * @param x - x position of entity in the grid
     * @param y - y position of entity in the grid
     * @param health - entity's health
     */
    public Health(int x, int y, int health){
        super(x, y);
        this.health = health;
    }

    /**
     * Method to get entity's health
     * @return entity's health
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * Method to print entity in the grid
     * @return representation of entity
     */
    @Override
    public String toString()
    {
        return "H";
    }
}