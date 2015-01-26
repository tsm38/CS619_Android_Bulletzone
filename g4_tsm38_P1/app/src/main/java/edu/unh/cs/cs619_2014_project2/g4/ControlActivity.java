/**********************************************************************************************
 Main Activity
 **********************************************************************************************/
package edu.unh.cs.cs619_2014_project2.g4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import edu.unh.cs.cs619_2014_project2.g4.game.ConstraintHandler;
import edu.unh.cs.cs619_2014_project2.g4.game.Entity;
import edu.unh.cs.cs619_2014_project2.g4.game.EntityFactory;
import edu.unh.cs.cs619_2014_project2.g4.game.SQLHandler;
import edu.unh.cs.cs619_2014_project2.g4.game.ShakeEventManager;
import edu.unh.cs.cs619_2014_project2.g4.game.SoundHandler;
import edu.unh.cs.cs619_2014_project2.g4.game.Tank;
import edu.unh.cs.cs619_2014_project2.g4.gui.GridAdapter;
import edu.unh.cs.cs619_2014_project2.g4.gui.UIEntity;
import edu.unh.cs.cs619_2014_project2.g4.gui.UIEntityFactory;
import edu.unh.cs.cs619_2014_project2.g4.server.BulletZoneRestClient;
import edu.unh.cs.cs619_2014_project2.g4.server.BusProvider;
import edu.unh.cs.cs619_2014_project2.g4.server.GridUpdateEvent;
import edu.unh.cs.cs619_2014_project2.g4.server.Poller;

    @EActivity(R.layout.activity_control)
    public class ControlActivity extends Activity implements ShakeEventManager.ShakeListener {
        private final static String LOG_TAG = "ControlActivity";

        private byte UP;
        private byte DOWN;
        private byte LEFT;
        private byte RIGHT;

        private Tank myTank;
        private long tankId = -1;
        private static final int LENGTH=257;
        @Bean SQLHandler sqlHandler;  //--------------------------------------------------------------sql
        @Bean SoundHandler soundHandler;
        @Bean ConstraintHandler constraintHandler;
        private int time=0;

        //variables checking for alive
        private boolean aliveFlag = false;
        private boolean doAliveFlagOnce = true;

        private int maxBullets = 2;
        private int deathTime; //what time to start replay at


    private ShakeEventManager shakeEventManager;
    private float x1, x2, y1, y2;
    Entity saveEntities[] =null;
        private boolean replay=false;



    @ViewById
    GridView gridView;

    @ViewById
    TextView textViewTankID;

    @RestService
    BulletZoneRestClient restClient;

    @ViewById
    Button bJoin;

    @ViewById
    Button bReplay;

    @ViewById
    Button bLeave;

    @Bean
    Poller poller;

    Bundle s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = savedInstanceState;
        sqlHandler = new SQLHandler(this); //-------------------------------------------------------sql
      //  soundHandler = new SoundHandler(this);
       // constraintHandler = new ConstraintHandler();
        soundHandler.create(this);

        UP = (byte) this.getResources().getInteger(R.integer.up);
        DOWN = (byte) this.getResources().getInteger(R.integer.down);
        LEFT = (byte) this.getResources().getInteger(R.integer.left);
        RIGHT = (byte) this.getResources().getInteger(R.integer.right);

        shakeEventManager = new ShakeEventManager();
        shakeEventManager.setListener(this);
        shakeEventManager.init(this);
       // sqlHandler.createTestSQLData();  //----------------------------------------------------------sql test
        sqlHandler.remakeDatabase();
        poller.sql=sqlHandler;
    }

    @Override protected void onRestart()
        {
            onCreate(s);
        }

        @Override
        protected void onResume() {
            super.onResume();
            BusProvider.getInstance().register(eventHandler);
        }

        @Override
        protected void onPause() {
            super.onPause();
            BusProvider.getInstance().unregister(eventHandler);
        }



        @Click(R.id.bLeave)
        public void leaveClicked(){ leave(); }


        @Click(R.id.bJoin)
        public void joinClicked(){
            if(replay)
                stopReplay();
            else
                join();
        }

        @Click(R.id.bReplay)
        public void replayClicked(){
            replay();
        }



        @UiThread  void updateTankId(String text){
        textViewTankID.setText(text);
    }


    @UiThread
    void replay() {
        BusProvider.getInstance().register(eventHandler);
        replay=true;
        Log.d(LOG_TAG, "replay");
        poller.running=true;
        //poller.setTimeStart(-1); //for testing
        poller.setTimeStart(deathTime);
        poller.replay=true;
        // UIEntity[] grid=sqlHandler.getTimestamp(0);
        // gridView.setAdapter(new GridAdapter(this, grid));
        bReplay.setEnabled(false);
    }

        @UiThread
        void stopReplay() {
            bReplay.setEnabled(false);
            replay=false;
            poller.replay=false;
            join();
        }


        @UiThread
        public void setAliveState() {
            bJoin.setEnabled(false);
            poller.running=true;
            bLeave.setEnabled(true);
            bReplay.setEnabled(false);
            poller.replay=false;
        }

        @UiThread
        public void setDeadState() {
            if(myTank!=null) {
                bJoin.setEnabled(true);
                bLeave.setEnabled(false);
                bReplay.setEnabled(true);
                gridView.setAdapter(new GridAdapter(this,
                        new int[]{(16 * myTank.getX()) + myTank.getY() + 1}, true));
                BusProvider.getInstance().unregister(eventHandler);
                deathTime=time;
            }
        }

        private Object eventHandler = new Object()
        {
            @Subscribe
            public void onUpdateGrid(GridUpdateEvent event) {
                updateGrid(event);
            }
        };

        @UiThread
        void updateGrid(GridUpdateEvent event){
            if (tankId != -1 || replay) {
                int[][] grid = event.getGrid();

                EntityFactory entityFactory = new EntityFactory(grid, tankId);
                Entity arrayOfEntities[] = entityFactory.createEntities();

                if(!replay) {
                    saveEntities = arrayOfEntities;

                    if(myTank!=null)
                       dead_or_alive(arrayOfEntities);

                    saveMyTank(arrayOfEntities);
                    //  sqlHandler.saveTime(arrayOfEntities, LENGTH, time); //-----------------------------------------sql
                    //Log.d(LOG_TAG, "at time " + time);
                    time++; //-------------------------------------------------------------------------------------sql
                    addGrid(grid);
                }

                UIEntityFactory uIEntityFactory = new UIEntityFactory();
                UIEntity[] arrayOfUIEntities = uIEntityFactory.createUIEntities(arrayOfEntities);

                Log.d(LOG_TAG, "display "+time+" here");
                gridView.setAdapter(new GridAdapter(this, arrayOfUIEntities, false));
            }
        }

        @Background
        void addGrid(int[][] grid) {
            sqlHandler.addGrid(grid, time);
        }

        @Background
    void join(){
        try{
            tankId = restClient.join().result;
            soundHandler.goSound();
            updateTankId("Alive");
            poller.running=true;
            poller.setReplay(false);
            poller.doPoll();
            setAliveState();
            replay=false;
            BusProvider.getInstance().register(eventHandler);

            /*String str="0,1000,1000,1000,0,1000,0,0,1000,1000,0,0,0,0,0,0,0,0,0,0,0,1000,0,1000,12230034,0,0,0,0,0,0,0,0,0,1000,1000,0,0,0,1000,1000,0,0,0,0,0,0,0,0,1000,10300030,0,0,0,0,0,0,0,0,0,0,0,14780032,0,0,1000,1000,1000,0,1000,0,1000,0,1000,0,0,0,0,0,0,0,0,0,14800034,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,14820020,0,0,0,0,0,0,0,0,0,0,0,14810030,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,14770026,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,1000,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
            sqlHandler.addStr(str, -1);
            str="0,0,1000,1000,1000,0,1000,0,0,1000,1000,0,0,0,0,0,0,0,0,0,0,0,1000,1000,12230034,1000,0,0,0,0,0,0,0,0,0,1000,1000,0,0,0,1000,1000,0,0,0,0,0,0,0,1000,10300030,0,0,0,0,0,0,0,0,0,0,0,14780032,0,0,0,1000,1000,1000,0,1000,0,1000,1000,1000,0,0,0,0,0,0,0,0,14800034,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,14820020,0,0,0,0,0,0,0,0,0,0,0,14810030,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,14770026,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,1000,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
            sqlHandler.addStr(str, -2);
            str="0,0,0,1000,1000,1000,0,1000,0,0,1000,1000,0,0,0,0,0,0,0,0,0,0,1000,1000,12230034,0,1000,0,0,0,0,0,0,0,0,0,1000,1000,0,0,0,1000,1000,0,0,0,0,0,0,1000,10300030,0,0,0,0,0,0,0,0,0,0,0,14780032,0,0,0,0,1000,1000,1000,0,1000,0,1000,1000,1000,0,0,0,0,0,0,0,14800034,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,14820020,0,0,0,0,0,0,0,0,0,0,0,14810030,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,14770026,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1000,0,0,0,0,0,0,0,0,0,1000,1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
            sqlHandler.addStr(str, -3);*/
        } catch (Exception e){
            e.printStackTrace();
        }
    }

        @Background
    void fire(){
        Log.d(LOG_TAG, "fire");


        if(tankId!=-1 && saveEntities != null)
        {
            constraintHandler.fire(maxBullets, saveEntities, tankId, restClient);
            soundHandler.fireSound();
        }
    }

	@Background
    void leave(){
        Log.d(LOG_TAG, "leave");
        poller.running=false;
        if(tankId!=-1 || replay) {
            updateTankId("leave");
            replay=false;
            if(tankId!=-1)
                restClient.leave(tankId);
            tankId=-1;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }




        @Background
        public void dead_or_alive(Entity[] arrayOfEntities) {


            boolean trigger = false;
            for (Entity entity : arrayOfEntities) {


                if ( entity != null  && entity.getClass() != Tank.class)
                    continue;

                Tank t = (Tank) entity;
                if ((t != null ? t.getID() : 0) == tankId) {

                    trigger = true;
                    break;
                }
            }
          if (trigger)
              updateTankId("alive");
            else {
              updateTankId("dead");
              setDeadState();

              soundHandler.explosiveSound();
              tankId = -1;
          }
        }


    @Background
    public void saveMyTank(Entity[] arrayOfEntities) {
        if (tankId != -1) {
            for (Entity entity : arrayOfEntities) {
                if (entity != null && entity.getClass() != Tank.class)
                    continue;

                Tank t = (Tank) entity;
                if ((t != null ? t.getID() : 0) == tankId) {
                    myTank = t;
                    break;
                }
            }
        }
    }





        @Background
        @Override
        public void onShake() {
            fire();
        }


        public boolean onTouchEvent(MotionEvent touchevent) {
            if (saveEntities!= null)
                saveMyTank(saveEntities);
            switch (touchevent.getAction())
            {
                // when user first touches the screen we get x and y coordinate
                case MotionEvent.ACTION_DOWN:
                {
                    x1 = touchevent.getX();
                    y1 = touchevent.getY();
                    break;
                }
                case MotionEvent.ACTION_UP:
                {
                    x2 = touchevent.getX();
                    y2 = touchevent.getY();
                   // String str="  x1: "+x1+"  x2: "+x2+"  y1: "+y1+"  y2: "+y2;
                    String str="";

                    if ( ((x1-x2) < 50) && ((x1-x2) > -50) && ((y1-y2) < 50) && ((y1-y2) > -50) ) {
                        Toast.makeText(this, "Fire!" + str, Toast.LENGTH_LONG).show();
                        fire();
                    }
                   else if (x1<x2 && ((x2-x1) > (y1-y2)) && ((x2-x1) > (y2-y1))) {
                        Toast.makeText(this, "Swipe right" + str, Toast.LENGTH_LONG).show();
                        //Log.d(LOG_TAG, "Swipe right"+str);
                        //entityHandler.turnPlayerTank(RIGHT);
                        //restClient.turn(tankId, (byte)RIGHT);

                        if(tankId!=-1) //do tests to see which way the tank should move here
                        {
                            constraintHandler.move(myTank, tankId, RIGHT, restClient);
                            soundHandler.moveSound();
                        }
                    }

                    else if (x1>x2 && (((x1-x2) > (y1-y2))) && ((x1-x2) > (y2-y1))) {
                        Toast.makeText(this, "Swipe left"+str, Toast.LENGTH_SHORT).show();
                        //Log.d(LOG_TAG, "Swipe left" + str);
                        //entityHandler.turnPlayerTank(LEFT);
                        //restClient.turn(tankId, (byte)LEFT);


                        if(tankId!=-1) //do tests to see which way the tank should move here
                        {
                            constraintHandler.move(myTank, tankId, LEFT, restClient);
                            soundHandler.moveSound();
                        }
                    }
                    else if (y1<y2 && (((y2-y1) > (x1-x2))) && ((y2-y1) > (x2-x1))) {
                        Toast.makeText(this, "Swipe down"+str, Toast.LENGTH_SHORT).show();
                        //Log.d(LOG_TAG, "Swipe down"+str);
                        //entityHandler.movePlayerTank(DOWN);
                        //restClient.move(tankId, (byte)DOWN);

                        if(tankId!=-1) //do tests to see which way the tank should move here
                        {
                            constraintHandler.move(myTank, tankId, DOWN, restClient);
                            soundHandler.moveSound();
                        }
                    }
                    else if (y1>y2 && (((y1-y2) > (x1-x2))) && ((y1-y2) > (x2-x1))) {
                        Toast.makeText(this, "Swipe up"+str, Toast.LENGTH_SHORT).show();
                        //Log.d(LOG_TAG, "Swipe up" + str);
                        //entityHandler.movePlayerTank(UP);
                        //restClient.move(tankId, (byte)UP);

                        if(tankId!=-1) //do tests to see which way the tank should move here
                        {
                            constraintHandler.move(myTank, tankId, UP, restClient);
                            soundHandler.moveSound();
                        }
                    }
                    break;
                }
            }
            return false;
        }



  }