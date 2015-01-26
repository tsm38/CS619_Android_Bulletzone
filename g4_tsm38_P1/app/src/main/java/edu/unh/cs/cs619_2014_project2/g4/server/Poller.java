package edu.unh.cs.cs619_2014_project2.g4.server;

/**
 * Created by Tay on 11/11/2014.
 */

import android.os.SystemClock;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import edu.unh.cs.cs619_2014_project2.g4.game.SQLHandler;

@EBean
public class Poller {
    private static final String LOG_TAG = "PollServer";
    public boolean replay=false; //true if getting replay events
    private int timeFrames=10; //number of replay frames to show
    private int timeIndex=0; //where we are in time
    private int timeStart=0; //where to start getting the time
    public SQLHandler sql;
    public boolean running=true;

    @RestService
    BulletZoneRestClient restClient;

    @Background(id = "grid_poller_task")
    public void doPoll() {
        int i=0;
        while (running) {
            if(replay) {
                //while(timeIndex>sql.maxTime) //for reverse
                    //timeIndex--;
                while(timeIndex<0)
                    timeIndex++;
                onGridUpdate(sql.getTimeWrapper(timeIndex));
                //timeIndex--; //for reverse
                timeIndex++;

                //Log.d(LOG_TAG, "timeindex: "+timeIndex+"   timestart-timeframes: "+(timeStart-timeFrames)+"  timestart: "+timeStart+"  timeFrames: "+timeFrames);
                //if(timeIndex<=(timeStart-timeFrames)) { //for reverse
                if(timeIndex>(timeStart+timeFrames)) {
                    timeIndex = timeStart;
                    SystemClock.sleep(1000);
                }
                SystemClock.sleep(1000);
            }
            else {
                onGridUpdate(restClient.grid());
            }

            // poll server every 100ms
            SystemClock.sleep(100);
        }
    }

    public void setTimeStart(int timeStart) {
        this.timeStart=timeStart-timeFrames;
        this.timeIndex=timeStart-timeFrames;
        if(timeStart==-1)
            timeFrames=3;
    }
    public void setSQL(SQLHandler sql) {
        this.sql=sql;
    }
    public void setReplay(boolean replay) {
        this.replay=replay;
    }

    @UiThread
    public void onGridUpdate(GridWrapper gw) {
        BusProvider.getInstance().post(new GridUpdateEvent(gw.getGrid(), gw.getTimeStamp()));
    }

}