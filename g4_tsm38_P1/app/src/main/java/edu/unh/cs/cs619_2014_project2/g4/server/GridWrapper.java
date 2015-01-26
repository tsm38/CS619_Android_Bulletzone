package edu.unh.cs.cs619_2014_project2.g4.server;

/**
 * Created by Tay on 11/6/2014.
 */

public	class	GridWrapper {
    private int[][] grid;
    private long timeStamp;

    public GridWrapper() {
    }

    public GridWrapper(int[][] grid) {
        this.grid = grid;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

  


}