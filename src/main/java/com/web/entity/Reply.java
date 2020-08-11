package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "user_id")
    public int userId;
    @Column(name = "doc_id")
    public int docId;
    @Column(name = "reply_id")
    public int replyId;
    @Column(name = "content", length = 200)
    public String content;
    @Column(name = "isReply") //是否是评论的评论，1表示是
    public boolean isReply;
    @Column(name = "date")
    public Date date;
    @Column(name = "likes")
    public int likes;

}