package edu.unh.cs.cs619_2014_project2.g4.game;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import edu.unh.cs.cs619_2014_project2.g4.server.GridWrapper;

@EBean
public class SQLHandler extends SQLiteOpenHelper {
    private static final String LOG_TAG="SQLHandler";
    private static final String DATABASE_NAME="EntityDB";

    private static final int UP=0;
    private static final int DOWN=4;
    private static final int LEFT=6;
    private static final int RIGHT=2;
    private static final int ENEMY=1;
    private static final int PLAYER=2;
    private static final int DATABASE_VERSION=1;
    private static final int WIDTH=16;
    private static final int HEIGHT=16;

    private static final String TANK_TABLE="tanks";
    private static final String WALL_TABLE="walls";
    private static final String BULLET_TABLE="bullets";
    private static final String BLANK_TABLE="blanks";
    private static final String INTS_TABLE="grid";

    public int maxTime=0;

    private Context context;
    private SQLiteDatabase writeDatabase;
    private SQLiteDatabase readDatabase;

    public SQLHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INTS_TABLE = "CREATE TABLE IF NOT EXISTS "+INTS_TABLE+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp INTEGER, "+
                "str VARCHAR(2000)" +
                //"str VARCHAR(700), " +
                //"x INTEGER, " +
                //"y INTEGER" +
                ")";

        String CREATE_TANK_TABLE = "CREATE TABLE IF NOT EXISTS "+TANK_TABLE+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tankID INTEGER, "+
                "timestamp INTEGER, " +
                "direction INTEGER, " +
                "player INTEGER, "+ //0=no, 1=yes
                "count INTEGER"+ //position in entity map thing
                ")";

        String CREATE_BULLET_TABLE = "CREATE TABLE IF NOT EXISTS "+BULLET_TABLE+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //"FOREIGN KEY(tankID) REFERENCES tank(tankID), " +
                "tankID INTEGER, " +
                "timestamp INTEGER, " +
                "count INTEGER" + //position in entity map thing
                ")";

        String CREATE_WALL_TABLE = "CREATE TABLE IF NOT EXISTS "+WALL_TABLE+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp INTEGER, " +
                "indestructible INTEGER, " + //0=no, 1=yes
                "count INTEGER" + //position in entity map thing
                ")";

        String CREATE_BLANK_TABLE = "CREATE TABLE IF NOT EXISTS "+BLANK_TABLE+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "timestamp INTEGER, " +
                "count INTEGER" + //position in entity map thing
                ")";

        db.execSQL(CREATE_INTS_TABLE);
        writeDatabase=db;
        //readDatabase=this.getReadableDatabase();

        /*database.execSQL(CREATE_TANK_TABLE);
        database.execSQL(CREATE_BULLET_TABLE);
        database.execSQL(CREATE_WALL_TABLE);
        database.execSQL(CREATE_BLANK_TABLE);*/
    }

    public void remakeDatabase() {
        if(readDatabase!=null && readDatabase.isOpen()) readDatabase.close();
        if(writeDatabase!=null && writeDatabase.isOpen()) writeDatabase.close();
        context.deleteDatabase(DATABASE_NAME);
        onUpgrade(this.getWritableDatabase(),1,1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+WALL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TANK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BULLET_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BLANK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+INTS_TABLE);
        onCreate(db);
    }

    public int getMaxTime() {
        return maxTime;
    }

    @Background
    public void addGrid(int[][] grid, int time) {
        //SQLiteDatabase database = this.getWritableDatabase();
        StringBuilder bigRow = new StringBuilder();
        boolean first=true;
        for(int i=0; i<WIDTH; i++) {
            for(int j=0; j<HEIGHT; j++) {
                //addInt(grid[i][j], i, j, time, database);
                if(first) {
                    bigRow.append(grid[i][j]);
                    first=false;
                }
                else {
                    bigRow.append(",");
                    bigRow.append(grid[i][j]);
                }
            }
        }
        if(time>maxTime) maxTime=time;
        //addStr(bigRow.toString(), time, database);

        ContentValues values = new ContentValues();
        values.put("timestamp", time);
        values.put("str", bigRow.toString());
        writeDatabase.insert(INTS_TABLE, null, values);

        //database.close();
        //Log.d(LOG_TAG, "0,0: "+grid[0][0]+"  1,1: "+grid[1][1]+"   adgdsdfgsdf "+bigRow.toString());
        //Log.d(LOG_TAG, "added time " + time);
    }
    public void addStr(String str, int time, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("timestamp", time);
        values.put("str", str);
        database.insert(INTS_TABLE, null, values);
    }
    public void addStr(String str, int time) {
        ContentValues values = new ContentValues();
        values.put("timestamp", time);
        values.put("str", str);
        writeDatabase.insert(INTS_TABLE, null, values);
    }
    @Background
    public void addInt(int add, int x, int y, int time, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("x",x);
        values.put("y",y);
        values.put("timestamp",time);
        values.put("str",add);
        database.insert(INTS_TABLE, null, values);
    }

    public GridWrapper getTimeWrapper(int time) {
        GridWrapper gw=new GridWrapper(getTime(time));
        Log.d(LOG_TAG, "got time "+time);
        return gw;
    }

    public int[][] getTime(int time) {
        int[][] grid = new int[WIDTH][HEIGHT];

        String timestampStr = Integer.toString(time);
        String[] args={timestampStr};
        String[] columns={"str"};

        if(readDatabase==null || !readDatabase.isOpen())
            readDatabase=this.getReadableDatabase();

        Cursor cursor = readDatabase.query(INTS_TABLE, //table
                columns,        //column names
                " timestamp=? ",    //selections
                args,               //selection args
                null,               //group by
                null,               //having
                null,               //order by
                null);              //limit
        if(cursor!=null && cursor.moveToFirst()) {
            String str=cursor.getString(0);
            String[] array = str.split(",", -1);
            int k=0;
            for(int i=0; i<WIDTH; i++) {
                for(int j=0; j<HEIGHT; j++) {
                    grid[i][j]=Integer.parseInt(array[k]);
                    k++;
                }
            }
        }
        //database.close();
        return grid;
    }










    /*
    public void addTank(Tank tank, int timestamp, int count) {
        int id=tank.getID();
        int direction=(int)tank.getDirection().code();
        boolean isEnemy=tank.getPlayerIsEnemy();
        addTank(id, direction, isEnemy, timestamp, count);
    }
    public void addTank(int id, int direction, boolean isEnemy, int timestamp, int count) {
        SQLiteDatabase database = this.getWritableDatabase();
        int player;
        if(isEnemy) player=0;
        else player=1;
        //Log.d(LOG_TAG, "id: " + id + "  player value: " + player);
        ContentValues values = new ContentValues();
        values.put("tankID",id);
        values.put("timestamp",timestamp);
        values.put("player",player);
        values.put("direction",direction);
        values.put("count",count);
        database.insert(TANK_TABLE, null, values);
        database.close();
    }

    public void addBlank(int timestamp, int count) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp",timestamp);
        values.put("count",count);
        database.insert(BLANK_TABLE, null, values);
        database.close();
    }

    public void addWall(Wall wall, int timestamp, int count) {
        addWall(wall.getHealth(), timestamp, count);
    }
    public void addIWall(IWall wall, int timestamp, int count) {
        addWall(-1, timestamp, count);
    }
    public void addWall(int hp, int timestamp, int count) {
        SQLiteDatabase database = this.getWritableDatabase();
        int indestructible;
        if(hp == -1) indestructible=1;
        else indestructible=0;

        ContentValues values = new ContentValues();
        values.put("timestamp",timestamp);
        values.put("indestructible",indestructible);
        values.put("count",count);

        database.insert(WALL_TABLE, null, values);
        database.close();
    }

    public void addBullet(Bullet bullet, int timestamp, int count) {
        int tankID=bullet.getTank();
        addBullet(tankID, timestamp, count);
    }
    public void addBullet(int tankID, int timestamp, int count) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp",timestamp);
        values.put("tankID",tankID);
        values.put("count",count);

        Log.d(LOG_TAG, "ADD BULLET "+tankID+"  "+count);

        database.insert(BULLET_TABLE, null, values);
        database.close();
    }


    public void saveTime(Entity[] grid, int length, int time) {
        for(int i=0; i<length; i++) {
            Entity current=grid[i];
            if( current != null && current.toString().equals("T"))
                addTank((Tank)current,time,i);
            else if( current != null && current.toString().equals("B"))
                addBullet((Bullet)current,time,i);
            else if( current != null && current.toString().equals("I"))
                addIWall((IWall)current,time,i);
            else if( current != null && current.toString().equals("W"))
                addWall((Wall)current,time,i);
            else if( current != null && current.toString().equals("blank"))
                addBlank(time,i);
            else addBlank(time, i);   //added null check and last else statement
        }
    }



    public UIEntity[] getTimestamp(int timestamp) {
        int width = WIDTH;
        int height = HEIGHT;
        int count;
        UIEntity[] entities = new UIEntity[257];
        SQLiteDatabase database = this.getReadableDatabase();

        String timestampStr = Integer.toString(timestamp);
        String[] args={timestampStr};
        String[] tankColumns={"tankID", "direction", "player", "count"};
        String[] bulletColumns={"tankID", "count"};
        String[] wallColumns={"indestructible", "count"};
        String[] blankColumns={"count"};

        Cursor cursor = database.query(TANK_TABLE, //table
                tankColumns,        //column names
                " timestamp=? ",    //selections
                args,               //selection args
                null,               //group by
                null,               //having
                null,               //order by
                null);              //limit
        if(cursor!=null && cursor.moveToFirst()) {
            putTank(entities, cursor);
            while(cursor.moveToNext()) {
                putTank(entities, cursor);
            }
        }
        cursor = database.rawQuery("select * from "+BULLET_TABLE+" where timestamp=?",args);
        if(cursor!=null && cursor.moveToFirst()) {
            putBullet(entities, cursor);
            while(cursor.moveToNext()) {
                putBullet(entities, cursor);
            }
        }

        cursor = database.query(WALL_TABLE, //table
                wallColumns,        //column names
                " timestamp=? ",    //selections
                args,               //selection args
                null,               //group by
                null,               //having
                null,               //order by
                null);              //limit
        if(cursor!=null && cursor.moveToFirst()) {
            putWall(entities, cursor);
            while(cursor.moveToNext()) {
                putWall(entities, cursor);
            }
        }

        cursor = database.query(BLANK_TABLE, //table
                blankColumns,        //column names
                " timestamp=? ",    //selections
                args,               //selection args
                null,               //group by
                null,               //having
                null,               //order by
                null);              //limit
        if(cursor!=null && cursor.moveToFirst()) {
            putBlank(entities, cursor);
            while(cursor.moveToNext()) {
                putBlank(entities, cursor);
            }
        }

        database.close();
        cursor.close();
        Log.d(LOG_TAG, "entity 32 = "+entities[32].toString());
        return entities;
    }


    private void putTank(UIEntity[] entities, Cursor cursor) {
        int tankID=Integer.parseInt(cursor.getString(0));
        int direction=Integer.parseInt(cursor.getString(1));
        int player=Integer.parseInt(cursor.getString(2));
        boolean isEnemy;
        isEnemy=(player==0);
        int count=Integer.parseInt(cursor.getString(3));
        entities[count]=new UITank(new Tank(0,0,10,tankID,direction,isEnemy));
    }
    private void putBullet(UIEntity[] entities, Cursor cursor) {
        //(int xPos, int yPos, int damage, int tankID)
        //if(cursor.getString(0)==null) return;
        Log.d(LOG_TAG, "bullet: "+cursor.getString(cursor.getColumnIndex("tankID"))+"  "+cursor.getString(cursor.getColumnIndex("count")));
        int tankID=Integer.parseInt(cursor.getString(cursor.getColumnIndex("tankID")));
        int count=Integer.parseInt(cursor.getString(cursor.getColumnIndex("count")));
        entities[count]=new UIBullet(new Bullet(0,0,5,tankID));
    }
    private void putWall(UIEntity[] entities, Cursor cursor) {
        //wall: (int x, int y, int health)
        //Iwall: (int x, int y)
        int indestructible=Integer.parseInt(cursor.getString(0));
        int count=Integer.parseInt(cursor.getString(1));
        if(indestructible==1)
            entities[count]=new UIWall(new IWall(0,0));
        else
            entities[count]=new UIWall(new Wall(0,0,10));
    }
    private void putBlank(UIEntity[] entities, Cursor cursor) {
        int count=Integer.parseInt(cursor.getString(0));
        entities[count]=new UIBlank(new Blank(0,0));
        //Log.d(LOG_TAG, "entities["+count+"] = "+entities[count].toString());
    }*/


    /*public void createTestSQLData() {
        remakeDatabase();

        for(int i=0; i<16; i++) {
            addWall(10,0,i);
        }
        for(int i=16; i<32; i++) {
            addWall(-1,0,i);
        }
        for(int i=32; i<50; i++) {
            addBlank(0, i);
        }

        addTank(1,UP,true,0,50);
        addTank(2,DOWN,false,0,51);

        for(int i=52; i<55; i++) {
            addBullet(1,0,i);
        }
        for(int i=55; i<(WIDTH*HEIGHT); i++) {
            addBlank(0, i);
        }
    }*/
}
