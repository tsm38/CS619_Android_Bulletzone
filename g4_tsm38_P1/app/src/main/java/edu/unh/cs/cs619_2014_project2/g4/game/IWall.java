package edu.unh.cs.cs619_2014_project2.g4.game;

/**
 * Class that defines an indestructible wall
 * @author Group 4
 */
public class IWall extends Entity  {

    /**
     * Class' constructor
     * @param x - x position of entity in the grid
     * @param y - y position of entity in the grid
     */
    public IWall(int x, int y){
            super(x, y);
        }

    /**
     * Method to print entity in the grid
     * @return representation of entity
     */
    @Override
    public String toString()
    {
        return "I";
    }

    }

