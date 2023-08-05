package com.y2m.bloodsugartwo;

import android.util.Log;

/**
 * Created by Mohamed Antar on 16-Mar-17.
 */
public class Item implements Comparable {
    private int id;
    private int timeInSeconds;
    private int value;
    private int type;



    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInMillSeconds) {
        this.timeInSeconds = timeInMillSeconds;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Item(int value, int timeInSeconds, int type) {
        this.value=value;
        this.timeInSeconds = timeInSeconds;
        this.type = type;
    }

    public Item(int id, int value,int timeInSeconds, int type) {
        this.id = id;
        this.timeInSeconds = timeInSeconds;
        this.value = value;
        this.type = type;
    }

    @Override
    public int compareTo(Object another) {
        int r=(this.timeInSeconds)- ((Item)another).getTimeInSeconds();
        Log.d("//////////  =","compareTo "+((Item)another).getTimeInSeconds());
        Log.d("//////////  =","compareTo "+(this.timeInSeconds));
        Log.d("//////////  =","compareTo "+r);
        return r;
    }
}