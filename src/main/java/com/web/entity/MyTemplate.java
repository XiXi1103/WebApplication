package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MyTemplate")
public class MyTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "user_id")
    public int userId;
    @Column(name = "doc_id")
    public int docId;
    @Column(name = "status") //true表示已经赞过
    public boolean status;
    @Column(name = "time") //true表示已经赞过
    public Date date;
}
