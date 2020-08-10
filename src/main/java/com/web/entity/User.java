package com.web.entity;



import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "Username",length = 20)
    public String username;
    @Column(name = "Password",length = 20)
    private String password;
    @Column(name = "ID",length = 20)
    private String ID;
    @Column(name = "Email")
    public String email;
    @Column(name = "PhoneNumber")
    public int phoneNumber;
    @Column(name = "CreateTime")
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