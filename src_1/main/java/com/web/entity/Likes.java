package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "user_id")
    public int userId;
    @Column(name = "doc_id")
    public int docId;
    @Column(name = "reply_id")
    public int replyId;
    @Column(name = "isDoc") //是reply还是doc，1表示是doc
    public boolean isDoc;
    @Column(name = "status") //true表示已经赞过
    public boolean status;
    @Column(name = "date")
    public Date date;
}