package com.hieubui.session;

import androidx.annotation.NonNull;

import java.util.List;

public class Contact {
    private long ID;

    private String name;

    private List<String> phoneNumbers;

    public Contact() {
    }

    public Contact(long ID, String name, List<String> phoneNumbers) {
        this.ID = ID;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @NonNull
    @Override
    public String toString() {
        return "Contact{ID='" + ID + '\'' + ", name='" + name + '\'' + ", phoneNumbers=" + phoneNumbers + '}';
    }
}
