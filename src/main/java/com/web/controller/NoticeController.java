package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupRepository;
import com.web.repository.ReplyRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@CrossOrigin
@Controller
public class NoticeController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    ReplyRepository replyRepository;
    public Notice addNotice(int userID,int informerID,int category,int ID){
        Notice notice = new Notice();
        notice.date = new Date();
        notice.docID = 0;
        notice.groupID = 0;
        notice.replyID = 0;
        notice.userID = userID;
        notice.informerID = informerID;
        User informer = userRepository.findUserById(informerID);
        if(informer == null){
            System.out.println(informerID);
        }
//        User user = userRepository.findUserById(userID);
        notice.category = category;//1 文档被评论 , 2 文档被点赞 , 3 评论被回复, 4 评论被点赞, 5 被邀请加入团队, 6 被踢出团队 , 7 被邀请协作文档, 8 被踢出协作文档
        switch(category){
            case 1:{
                Documentation documentation = documentationRepository.findDocumentationById(ID);
                notice.msg = informer.username + "评论了您的文档——" + documentation.title;
                notice.status = false;
                notice.docID = ID;
                break;
            }
            case 2:{
                Documentation documentation = documentationRepository.findDocumentationById(ID);
                notice.msg = informer.username + "点赞了您的文档——" + documentation.title;
                notice.status = false;
                notice.docID = ID;
                break;
            }
            case 3:{
                Reply reply = replyRepository.findReplyById(ID);
                notice.msg = informer.username + "回复了您的评论——" + reply.content.substring(0,Math.min(reply.content.length(),10));
                notice.status = false;
                notice.replyID = ID;
                break;
            }
            case 4:{
                Reply reply = replyRepository.findReplyById(ID);
                notice.msg = informer.username + "点赞了您的评论——" + reply.content.substring(0,Math.min(reply.content.length(),10));
                notice.status = false;
                notice.replyID = ID;
                break;
            }
            case 5:{
                Group group = groupRepository.findGroupById(ID);
                notice.msg = informer.username + "邀请您加入团队——" + group.groupName;
                notice.status = false;
                notice.groupID = ID;
                break;
            }
            case 6:{
                Group group = groupRepository.findGroupById(ID);
                notice.msg = informer.username + "将您踢出了团队——" + group.groupName;
                notice.status = false;
                notice.groupID = ID;
                break;
            }
            case 7:{
                Documentation documentation = documentationRepository.findDocumentationById(ID);
                notice.msg = informer.username + "邀请您加入协作文档——" + documentation.title;
                notice.status = false;
                notice.docID = ID;
                break;
            }
            case 8:{
                Documentation documentation = documentationRepository.findDocumentationById(ID);
                notice.msg = informer.username + "将您踢出了协作文档——" + documentation.title;
                notice.status = false;
                notice.docID = ID;
                break;
            }
            default:{
                notice.msg = "未知错误！";
                notice.status = false;
            }
        }
        return notice;
    }

}
