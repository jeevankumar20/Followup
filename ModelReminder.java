package com.example.newapp;

public class ModelReminder {
    private String contactName;
    private int id;

    public ModelReminder(int id) {this.id = id;}
    public ModelReminder(String contactName) {
        this.contactName = contactName;
    }
    public ModelReminder(int id, String contactName) {
        this.id = id;
        this.contactName = contactName;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}

