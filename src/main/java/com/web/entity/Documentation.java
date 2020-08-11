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
    @Column(name = "content",length = 10240)
    public String content;
    @Column(name = "group_id")
    public int groupId;
    @Column(name = "other_permission")
    public int otherPermission;
    @Column(name = "recycled")
    public boolean recycled;

}
