package com.web.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "group_name", length = 20)
    public String groupName;
    @Column(name = "create_time")
    public Date createTime;
    @Column(name = "creator_id")
    public int creatorId;
    @Column(name = "information", length = 200)
    public String information;

    public int getId() {
        return id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        groupName = groupName;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int createrid) {
        this.creatorId = createrid;
    }
}