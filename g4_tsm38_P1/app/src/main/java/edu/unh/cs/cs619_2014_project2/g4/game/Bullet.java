package edu.unh.cs.cs619_2014_project2.g4.game;

/**
 * Class that defines a bullet
 * @author Group 4
 */
public class Bullet extends Entity
{

    private int m_damage;
    private int m_tank;


    /**
     * Class' Constructor
     * @param xPos - x position of entity in the grid
     * @param yPos - y position of entity in the grid
     * @param damage - damage bullet can cause
     * @param tankID - ID of tank that fire bullet
     */
    public Bullet(int xPos, int yPos, int damage, int tankID)
    {
        super(xPos, yPos);
        m_damage = damage;
        m_tank = tankID;
    }

    /**
     * Method to get the ID of tank that fired the bullet
     * @return tank ID
     */
    public int getTank()
    {
        return m_tank;
    }

    /**
     * Method to get how much damage was caused in an entity
     * @return damage
     */
    public int getDamage()
    {
        return m_damage;
    }

    /**
     * Method to print entity in the grid
     * @return representation of entity
     */
    @Override
    public String toString()
    {
      return "B";
    }
}