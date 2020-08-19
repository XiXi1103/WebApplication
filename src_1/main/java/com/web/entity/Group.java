package com.web.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "team")
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
    @Column(name = "content", length = 200)
    public String content;

}