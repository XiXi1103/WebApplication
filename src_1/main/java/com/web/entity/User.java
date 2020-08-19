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
    @Column(name = "email")
    public String email;
    @Column(name = "phone_number")
    public String phoneNumber;
    @Column(name = "create_time")
    public Date createTime;
    @Column(name = "recently_used1")
    public int recently_used1;
    @Column(name = "recently_used2")
    public int recently_used2;
    @Column(name = "recently_used3")
    public int recently_used3;
    @Column(name = "recently_used4")
    public int recently_used4;
    @Column(name = "recently_used5")
    public int recently_used5;
    @Column(name = "recently_usednum")
    public int recently_usednum;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getRecently_used1() {
        return recently_used1;
    }

    public void setRecently_used1(int recently_used1) {
        this.recently_used1 = recently_used1;
    }

    public int getRecently_used2() {
        return recently_used2;
    }

    public void setRecently_used2(int recently_used2) {
        this.recently_used2 = recently_used2;
    }

    public int getRecently_used3() {
        return recently_used3;
    }

    public void setRecently_used3(int recently_used3) {
        this.recently_used3 = recently_used3;
    }

    public int getRecently_used4() {
        return recently_used4;
    }

    public void setRecently_used4(int recently_used4) {
        this.recently_used4 = recently_used4;
    }

    public int getRecently_used5() {
        return recently_used5;
    }

    public void setRecently_used5(int recently_used5) {
        this.recently_used5 = recently_used5;
    }

    public int getRecently_usednum() {
        return recently_usednum;
    }

    public void setRecently_usednum(int recently_usednum) {
        this.recently_usednum = recently_usednum;
    }
}