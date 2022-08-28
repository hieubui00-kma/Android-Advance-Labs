package com.hieubui.session;

import androidx.annotation.NonNull;

import java.util.Date;

public class SMSMessage {
    private int ID;

    private String address;

    private String subject;

    private String body;

    private Date dateSent;

    public SMSMessage() {
    }

    public SMSMessage(int ID, String address, String subject, String body, Date dateSent) {
        this.ID = ID;
        this.address = address;
        this.subject = subject;
        this.body = body;
        this.dateSent = dateSent;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    @NonNull
    @Override
    public String toString() {
        return "SMSMessage{" +
            "ID=" + ID +
            ", address='" + address + '\'' +
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            ", dateSent=" + dateSent +
            '}';
    }
}
