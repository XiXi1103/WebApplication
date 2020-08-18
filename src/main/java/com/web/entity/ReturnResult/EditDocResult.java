package com.web.entity.ReturnResult;

public class EditDocResult {
    public String content;
    public String html;
    public int docID;
    public int userPermission;//当前用户对文档的权限
    public int currentPermission;//文档当前权限
    public String title ;
    public boolean success;
    public String msg;
}
