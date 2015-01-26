package edu.unh.cs.cs619_2014_project2.g4.game;

/**
 * Class that defines the entities and what is in common among them
 * @author Group 4
 */
public abstract class Entity
{

    private int m_xPos;
    private int m_yPos;


    /**
     * Class' constructor
     * @param xPos - x position of entity in the grid
     * @param yPos - y position of entity in the grid
     */
    public Entity(int xPos, int yPos)
    {
        m_xPos = xPos;
        m_yPos = yPos;
    }


    /**
     * Method that gets entity's X value in the grid
     * @return x value
     */
    public int getX()
    {
        return m_xPos;
    }

    /**
     * Method that gets entity's Y value in the grid
     * @return y value
     */
    public int getY()
    {
        return m_yPos;
    }

    /**
     * Method to print entity in the grid
     * @return representation of entity
     */
    @Override
    public String toString()
    {
        return "E";
    }
}