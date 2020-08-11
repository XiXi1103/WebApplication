package com.web.entity;

public class Reply_vue {
    public int userId;
    public int docId;
    public int replyId;
    public int id; //删除reply时将该reply id传入
    public boolean isReply;
    public String content;
}
