package com.hieubui.consumer;

public class Movie {
    private int ID;

    private String name;

    private String description;

    public Movie() {
    }

    public Movie(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "ID=" + ID +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            '}';
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
