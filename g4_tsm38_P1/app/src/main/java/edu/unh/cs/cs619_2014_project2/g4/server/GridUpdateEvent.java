package edu.unh.cs.cs619_2014_project2.g4.server;

/**
 * Created by Tay on 11/11/2014.
 */
public class GridUpdateEvent {
    int [][] grid;
    long timestamp;

    public GridUpdateEvent(int[][] grid, long timestamp) {
        this.grid = grid;
        this.timestamp = timestamp;
    }

    public int[][] getGrid() {
        return grid;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
