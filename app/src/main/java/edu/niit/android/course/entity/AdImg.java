package edu.niit.android.course.entity;

import java.io.Serializable;

public class AdImg implements Serializable {
    private int id;
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
