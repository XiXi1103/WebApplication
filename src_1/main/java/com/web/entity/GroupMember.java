package com.web.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "GroupMember")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "UserId", length = 20)
    public int userId;
    @Column(name = "GroupId", length = 20)
    public int groupId;
    @Column(name = "permission")
    public int permission;

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userid) {
        this.userId = userid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupid) {
        this.groupId = groupid;
    }

    public Date getJoin_time() {
        return join_time;
    }

    public void setJoin_time(Date join_time) {
        this.join_time = join_time;
    }

    @Column(name = "join_time")
    public Date join_time;
}