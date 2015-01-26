package edu.unh.cs.cs619_2014_project2.g4.game;


/**
 * Class that defines a blank area in the grid, showing that there's no entity
 * @author Group 4
 */
public class Blank extends Entity
{
    /**
     * Class' constructor
     * @param xPos - x position of entity in the grid
     * @param yPos - y position of entity in the grid
     */
    public Blank(int xPos, int yPos)
    {
        super(xPos, yPos);
    }


    /**
     * Method to print entity in the grid
     * @return representation of entity
     */
    @Override
    public String toString()
    {
        return "blank";
    }
}
