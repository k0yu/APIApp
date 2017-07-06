package com.example.y_saito.networkapplication;

import java.io.Serializable;
import java.util.PriorityQueue;

/**
 * Created by y-saito on 2017/07/05.
 */

public class Event implements Serializable {
    private String title;
    private int id;
    private String catchCopy;
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setCatchCopy(String catchCopy) {
        this.catchCopy = catchCopy;
    }

    public String getCatchCopy() {
        return catchCopy;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
