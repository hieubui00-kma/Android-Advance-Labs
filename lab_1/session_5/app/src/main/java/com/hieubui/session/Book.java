package com.hieubui.session;

public class Book {
    private int ID;

    private String title;

    private String introduce;

    private String summary;

    public Book() {
    }

    public Book(int ID, String title, String introduce, String summary) {
        this.ID = ID;
        this.title = title;
        this.introduce = introduce;
        this.summary = summary;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
