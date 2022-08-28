package com.hieubui.session;

import androidx.annotation.NonNull;

public class Author {
    private int ID;

    private String name;

    public Author() {
    }

    public Author(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Author{" +
            "ID=" + ID +
            ", name='" + name + '\'' +
            '}';
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
}
