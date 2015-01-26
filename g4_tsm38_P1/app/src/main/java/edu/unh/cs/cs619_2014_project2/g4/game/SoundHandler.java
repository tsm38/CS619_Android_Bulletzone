package edu.unh.cs.cs619_2014_project2.g4.game;

import android.content.Context;
import android.media.MediaPlayer;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import edu.unh.cs.cs619_2014_project2.g4.R;

/**
 * Created by Tay on 11/29/2014.
 *
 *
 * now if we want to add a sound, we just modify this class
 */
@EBean
public class SoundHandler {

    private Context context;
    MediaPlayer fire;
    MediaPlayer explosive;
    MediaPlayer turn;
    MediaPlayer move;
    MediaPlayer go;


@Background
    public void create(Context context) {
        this.context = context;
        fire = MediaPlayer.create(context, R.raw.fire);
        explosive = MediaPlayer.create(context, R.raw.explosion);
        turn = MediaPlayer.create(context, R.raw.turn);
        move = MediaPlayer.create(context, R.raw.move);
        go = MediaPlayer.create(context, R.raw.go);

    }

@UiThread
    public void explosiveSound() {

        if (fire.isPlaying())
            fire.pause();
        else if (turn.isPlaying())
            turn.pause();
        else if (move.isPlaying())
            move.pause();
        else if (explosive.isPlaying())
            explosive.pause();
        else if (go.isPlaying())
            go.pause();


        explosive.start();

    }
    @UiThread
    public void fireSound() {

        if (fire.isPlaying())
            fire.pause();
        else if (turn.isPlaying())
            turn.pause();
        else if (move.isPlaying())
            move.pause();
        else if (explosive.isPlaying())
            explosive.pause();
        else if (go.isPlaying())
            go.pause();


        fire.start();

    }

    @UiThread
    public void moveSound() {

        if (fire.isPlaying())
            fire.pause();
        else if (turn.isPlaying())
            turn.pause();
        else if (move.isPlaying())
            move.pause();
        else if (explosive.isPlaying())
            explosive.pause();
        else if (go.isPlaying())
            go.pause();

        move.start();
    }

    @UiThread
    public void turnSound() {

        if (fire.isPlaying())
            fire.pause();
        else if (turn.isPlaying())
            turn.pause();
        else if (move.isPlaying())
            move.pause();
        else if (explosive.isPlaying())
            explosive.pause();
        else if (go.isPlaying())
            go.pause();


        move.start();
    }

    @UiThread
    public void goSound() {

        if (fire.isPlaying())
            fire.pause();
        else if (turn.isPlaying())
            turn.pause();
        else if (move.isPlaying())
            move.pause();
        else if (explosive.isPlaying())
            explosive.pause();
        else if (go.isPlaying())
            go.pause();


        go.start();
    }
}

