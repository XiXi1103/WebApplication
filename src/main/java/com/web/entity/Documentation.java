package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documentation")
public class Documentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "title", length = 20)
    public String title;
    @Column(name = "create_time")
    public Date createTime;
    @Column(name = "creator_id")
    public int creatorId;
    @Column(name = "path",length = 100)
    public String path;
    @Column(name = "group_id")
    public int groupId;
    @Column(name = "other_permission")
    public int otherPermission;
    @Column(name = "isTrash")
    public boolean isTrash; //true表示在回收站里
    @Column(name = "last_time")
    public Date lastTime;
    @Column(name = "isEdit")
    public boolean isEdit; //是否在修改
    @Column(name = "editorId")
    public int editorId; //修改者id
    @Column(name = "editorNum")
    public int editorNum; //修改次数
    @Column(name = "isTemplate")
    public boolean isTemplate; //是否是模板
//    @Column(name = "abstract")
//    public String abstract;
}
