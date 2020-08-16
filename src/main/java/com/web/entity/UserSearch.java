package com.web.entity;

public class UserSearch {
    public String username;
    public int id;

    public UserSearch() {}

    public UserSearch(int id, String username) {
        this.username = username;
        this.id = id;
    }
}
