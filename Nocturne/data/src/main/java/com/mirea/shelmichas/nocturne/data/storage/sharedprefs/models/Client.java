package com.mirea.shelmichas.nocturne.data.storage.sharedprefs.models;

public class Client {
    private String name;
    private String phone;

    public Client(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}