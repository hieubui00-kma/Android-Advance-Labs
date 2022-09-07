package com.hieubui.session;

import android.graphics.Bitmap;

public class User {
    private int ID;

    private String name;

    private Bitmap avatar;

    public User() {
    }

    public User(int ID, String name, Bitmap avatar) {
        this.ID = ID;
        this.name = name;
        this.avatar = avatar;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
