package edu.unh.cs.cs619_2014_project2.g4.server;

/**
 * Created by Tay on 11/11/2014.
 */
import com.google.common.eventbus.EventBus;

public class BusProvider {
    final static EventBus bus = new EventBus();

    public static EventBus getInstance(){
        return bus;
    }

    private BusProvider(){
    }
}