package com.web.entity;

public class UserSearch {
    public String name;
    public int id;

    public UserSearch() {}

    public UserSearch(int id, String username) {
        this.name = username;
        this.id = id;
    }
}
