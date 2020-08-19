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
    public int category;
    //1 文档被评论 , 2 文档被点赞 ,3 被邀请协作文档, 4 被踢出协作文档, 5 协作文档被修改/被删除（查询不到就是被删除了），6 退出协作文档，7 评论被回复, 8 评论被点赞，9 被邀请加入团队, 10 被踢出团队
    //11 团队文档被修改/被删除（查询不到就是被删除了），12 成员退出团队
    @Column(name = "doc_id")
    public int docID;
    @Column(name = "reply_id")//被回复
    public int replyID;
    @Column(name = "reply_id_add")//回复
    public int replyID_add;
    @Column(name = "group_id")
    public int groupID;
    @Column(name = "about")
    public int about;
    @Column(name = "status")
    public boolean status;//true 表示 已被查看
    @Column(name = "success")
    public boolean success;//true 表示 通知生成成功
}
