package com.example.crudapp;

public class UserDeleteRequest {
    private int id;

    public UserDeleteRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
