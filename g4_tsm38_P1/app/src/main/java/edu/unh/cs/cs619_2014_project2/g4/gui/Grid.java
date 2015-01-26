package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import java.util.ArrayList;

import edu.unh.cs.cs619_2014_project2.g4.game.Entity;

/**
 * Created by Tay on 11/13/2014.
 */

public class Grid extends GridView {
    private ArrayList<Entity> m_entities;
    private float x1, x2, y1, y2;
    private static final String LOG_TAG = "Grid";
    private Context context;

    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        m_entities = new ArrayList<Entity>();
    }
    public ArrayList<Entity> getEntities()
        {
            return m_entities;
        }
}
