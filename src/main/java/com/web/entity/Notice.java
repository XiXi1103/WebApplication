package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "user_id")
    public int userID;
    @Column(name = "date")
    public Date date;
    @Column(name = "informer_id")//发出通知的人
    public int informerID;
    @Column(name = "msg")
    public String msg;
    @Column(name = "category")
    public int category; //1 文档被评论 , 2 文档被点赞 , 3 评论被回复, 4 评论被点赞, 5 被邀请加入团队, 6 被踢出团队 , 7 被邀请协作文档, 8 被踢出协作文档
    @Column(name = "doc_id")
    public int docID;
    @Column(name = "reply_id")
    public int replyID;
    @Column(name = "group_id")
    public int groupID;
    @Column(name = "status")
    public boolean status;//true 表示 已被查看
}
