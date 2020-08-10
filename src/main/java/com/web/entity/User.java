package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "username",length = 20)
    public String username;
    @Column(name = "password",length = 20)
    public String password;
    @Column(name = "userid",length = 20)
    public String ID;
    @Column(name = "email")
    public String email;
    @Column(name = "phone_number")
    public String phoneNumber;
    @Column(name = "create_time")
    public Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}