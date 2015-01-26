package edu.unh.cs.cs619_2014_project2.g4.gui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import edu.unh.cs.cs619_2014_project2.g4.R;

/**
 * Created by Tay on 11/14/2014.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;

    UIEntity[] entities = new UIEntity[257];
    int[] x;// = new int[257];
    boolean dead = false;


    public GridAdapter(Context context, UIEntity[] entities, boolean dead) {
        this.context = context;
        this.entities = entities;
        this.dead = dead;
    }

    public GridAdapter(Context context, int[] x, boolean dead) {
        this.context = context;
        this.x = x;
        this.dead = dead;
    }



    public View getView(int position, View convertView, ViewGroup parent) {




        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);
            gridView = inflater.inflate(R.layout.grid, null);

            // set value into textview
        //    TextView textView = (TextView) gridView.findViewById(R.id.gridText);


            // set value into textview
            ImageView imageview = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);


            if (dead) {
                if (position == x[0]-1) {

                    imageview.setImageResource(R.drawable.redx);
                }
            }
else {
                if (position == 256) {
                } else {
                    if (entities[position] == null)
                        Log.d("GridAdapter", "entities[" + position + "]is null");
                    imageview.setImageResource(entities[position].getImage());
                    imageview.setFocusable(false);
                    // textView.setText(entities[position].toString());
                }

            }



        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        if (dead)
            return x[0];
        else
        return entities.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}