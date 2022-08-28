package com.hieubui.session;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;

    private String artistNames;

    private String fileName;

    public Song() {
    }

    public Song(String title, String artistNames) {
        this.title = title;
        this.artistNames = artistNames;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(String artistNames) {
        this.artistNames = artistNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
