package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.NoticeResult;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@Controller
public class NoticeController {
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @Autowired
    CollaboratorRepository collaboratorRepository;
    @Autowired
    NoticeRepository noticeRepository;
    //1 文档被评论 , 2 文档被点赞 ,3 被邀请协作文档, 4 被踢出协作文档, 5 协作文档被修改/被删除（查询不到就是被删除了），6 退出协作文档，
    // 7 评论被回复, 8 评论被点赞，9 被邀请加入团队, 10 被踢出团队
    //11 团队文档被修改/被删除（查询不到就是被删除了），12 成员退出团队
//    public  Notice addNotice(int userID,int informerID,int category,
//                             int docID,int replyID,int groupID,int specialID,//前三个ID是受体，specialID是主体
//                             GroupRepository groupRepository1,UserRepository userRepository1,
//                             DocumentationRepository documentationRepository1,
//                             ReplyRepository replyRepository1){
//        String[] inform = {"评论了您的文档:",
//                "点赞了您的文档:",
//                "被邀请协作文档:",
//                "被踢出协作文档:",
//                "协作文档被",
//                "退出协作文档:",
//                "评论被回复:",
//                "评论被点赞:",
//                "被邀请加入团队:",
//                "被踢出团队:",
//                "团队文档被:",
//                "成员退出团队:"
//        };
//        Notice notice = new Notice();
//        notice.date = new Date();
//        notice.docID = docID;
//        notice.groupID = groupID;
//        notice.replyID = replyID;
//        notice.userID = userID;
//        notice.specialID = specialID;
//        notice.informerID = informerID;
//        notice.status = false;
//        notice.success = false;
//        User informer = userRepository1.findUserById(informerID);
//        if(informer == null){
//            return null;
//        }
////        User user = userRepository.findUserById(userID);
//        notice.category = category;
//        //1 文档被评论 , 2 文档被点赞 ,3 被邀请协作文档, 4 被踢出协作文档, 5 协作文档被修改/被删除（查询不到就是被删除了），6 退出协作文档，
//        // 7 评论被回复, 8 评论被点赞，9 被邀请加入团队, 10 被踢出团队
//        //11 团队文档被修改/被删除（查询不到就是被删除了），12 成员退出团队
//        switch(category){
//            case 1:{
//                Documentation documentation = documentationRepository1.findDocumentationById(docID);
//                Reply reply = replyRepository1.findReplyById(specialID);
//                if(documentation == null || reply == null){
//                    notice.msg = "文档或回复已被删除";
//                }
//                else {
//                    notice.msg = informer.username + inform[category - 1] + documentation.title;
//                    notice.success = true;
//                }
//                break;
//            }
//            case 2:{
//                Documentation documentation = documentationRepository1.findDocumentationById(docID);
//                if(documentation == null){
//                    notice.msg = "文档已被删除";
//                }
//                else {
//                    notice.msg = informer.username + inform[category - 1] + documentation.title;
//                }
//                break;
//            }
//            case 3:{
//                Reply reply = replyRepository1.findReplyById(ID);
//                notice.msg = informer.username + inform[category-1] + reply.content.substring(0,Math.min(reply.content.length(),10));
//                break;
//            }
//            case 4:{
//                Reply reply = replyRepository1.findReplyById(ID);
//                notice.msg = informer.username + inform[category-1] + reply.content.substring(0,Math.min(reply.content.length(),10));
//                break;
//            }
//
//            case 3:{
//                Reply reply = replyRepository1.findReplyById(ID);
//                notice.msg = informer.username + inform[category-1] + reply.content.substring(0,Math.min(reply.content.length(),10));
//                break;
//            }
//            case 4:{
//                Reply reply = replyRepository1.findReplyById(ID);
//                notice.msg = informer.username + inform[category-1] + reply.content.substring(0,Math.min(reply.content.length(),10));
//                break;
//            }
//            case 5:{
//                Group group = groupRepository1.findGroupById(ID);
//                notice.msg = informer.username + "邀请您加入团队——" + group.groupName;
//                break;
//            }
//            case 6:{
//                Group group = groupRepository1.findGroupById(ID);
//                notice.msg = informer.username + "将您踢出了团队——" + group.groupName;
//                break;
//            }
//            case 7:{
//                Documentation documentation = documentationRepository1.findDocumentationById(ID);
//                notice.msg = informer.username + "邀请您加入协作文档——" + documentation.title;
//                break;
//            }
//            case 8:{
//                Documentation documentation = documentationRepository1.findDocumentationById(ID);
//                notice.msg = informer.username + "将您踢出了协作文档——" + documentation.title;
//                break;
//            }
//            default:{
//                notice.msg = "未知错误！";
//                notice.status = false;
//            }
//        }
//        return notice;
//    }


//   1 文档被评论 , 2 文档被点赞 ,3 邀请协作文档, 4 踢出协作文档, 5 协作文档被修改, 6 协作文档被删除，7 退出协作文档，
//    finished 1,2,3,4,7
    public Notice addNoticeAboutDoc(int userID,int informerID,int category,
                                    int docID,int replyID,
                                    UserRepository userRepository1,
                                    DocumentationRepository documentationRepository1,
                                    ReplyRepository replyRepository1){
        //初始化
        Notice notice = new Notice();
        notice.date = new Date();
        notice.docID = docID;
        notice.groupID = 0;
        notice.replyID = replyID;
        notice.replyID_add = 0;
        notice.userID = userID;
        notice.informerID = informerID;
        notice.status = false;
        notice.success = false;
        notice.about = 1;

        String[] inform = {"",
                "评论了您的文档:",
                "点赞了您的文档:",
                "邀请您协作文档:",
                "将您踢出协作文档:",
                "修改了协作文档:",
                "删除了协作文档:",
                "退出了协作文档:"
        };
        User informer = userRepository1.findUserById(informerID);
        Documentation documentation = documentationRepository1.findDocumentationById(docID);
        Reply reply = replyRepository1.findReplyById(replyID);
        if(informer == null || documentation == null || category <= 0 || category >= 8){
            notice.msg = "Unknown error happen!";
            return notice;
        }
        notice.msg = informer.username + inform[category] + documentation.title;
        if(category == 1){
            if(reply == null){
                notice.msg = "Unknown error happen!";
                return notice;
            }
            else{
                notice.msg += "——" + reply.content.substring(Math.min(10,reply.content.length()));
            }
        }
        notice.success = true;
        return notice;
    }

    //1 回复评论，2 点赞评论
    //    finished 1,2
    public Notice addNoticeAboutReply(int userID,int informerID,int category,
                                      int replyID1,int replyID2,
                                      UserRepository userRepository1,
                                      DocumentationRepository documentationRepository1,
                                      ReplyRepository replyRepository1){
        //初始化
        Notice notice = new Notice();
        notice.date = new Date();
        notice.docID = replyRepository1.findReplyById(replyID1).docId;
        notice.groupID = 0;
        notice.replyID = replyID1;
        notice.replyID_add = replyID2;
        notice.userID = userID;
        notice.informerID = informerID;
        notice.status = false;
        notice.success = false;
        notice.about = 2;

        String[] inform = {"",
                "回复了您的评论:",
                "点赞了您的评论:",
        };
        User informer = userRepository1.findUserById(informerID);
        Reply reply1 = replyRepository1.findReplyById(replyID1);
        Reply reply2 = replyRepository1.findReplyById(replyID2);
        if(informer == null || reply1 == null ||category <= 0 || category >= 3){
            notice.msg = "Unknown error happen!";
            return notice;
        }
        notice.msg = informer.username + inform[category] + reply1.content.substring(Math.min(10,reply1.content.length()));
        if(category == 1){
            if(reply2 == null){
                notice.msg = "Unknown error happen!";
                return notice;
            }
            else{
                notice.msg += "——" + reply2.content.substring(Math.min(10,reply2.content.length()));
            }
        }
        notice.success = true;
        return notice;
    }

//    1 邀请加入团队, 2 被踢出团队 , 3 成员退出团队,4 团队被解散
//    finished 1,2,3,4
    public Notice addNoticeAboutGroup(int userID,int informerID,int category,
                                    int groupID,
                                    UserRepository userRepository1,
                                      GroupRepository groupRepository1
                                    ){
        //初始化
        Notice notice = new Notice();
        notice.date = new Date();
        notice.docID = 0;
        notice.groupID = groupID;
        notice.replyID = 0;
        notice.replyID_add = 0;
        notice.userID = userID;
        notice.informerID = informerID;
        notice.status = false;
        notice.success = false;
        notice.about = 3;
        notice.category = category;
        String[] inform = {"",
                "邀请您加入团队:",
                "将您踢出团队:",
                "退出了团队:",
                "解散了团队:",
        };
        User informer = userRepository1.findUserById(informerID);
        Group group = groupRepository1.findGroupById(groupID);
        if(informer == null || group == null || category <= 0 || category >= 5){
            notice.msg = "Unknown error happen!";
            return notice;
        }
        notice.msg = informer.username + inform[category] + group.groupName;
        notice.success = true;
        return notice;
    }

    //    1 团队文档被修改 2 被删除
//    finished 1,2
    public Notice addNoticeAboutGroupDoc(int userID,int informerID,int category,
                                         int groupID,int docID,
                                         UserRepository userRepository1,
                                         DocumentationRepository documentationRepository1,
                                         GroupRepository groupRepository1
    ){
        //初始化
        Notice notice = new Notice();
        notice.date = new Date();
        notice.docID = docID;
        notice.groupID = groupID;
        notice.replyID = 0;
        notice.replyID_add = 0;
        notice.userID = userID;
        notice.informerID = informerID;
        notice.status = false;
        notice.success = false;
        notice.about = 4;

        String[] inform = {"",
                "修改了团队文档:",
                "删除了团队文档:",
        };
        User informer = userRepository1.findUserById(informerID);
        Group group = groupRepository1.findGroupById(groupID);
        Documentation documentation = documentationRepository1.findDocumentationById(docID);
        if(informer == null || group == null || documentation == null || category <= 0 || category >= 4){
            notice.msg = "Unknown error happen!";
            return notice;
        }
        notice.msg = informer.username + inform[category] + documentation.title;
        notice.success = true;
        return notice;
    }
    //    1 团队权限被修改 2 协作文档权限被修改
    //    finished 1,2
    public Notice addNoticeAboutPermission(int userID,int informerID,int category,
                                           int groupID,int docID,int permission,
                                           UserRepository userRepository1,
                                           DocumentationRepository documentationRepository1,
                                           GroupRepository groupRepository1,
                                           GroupMemberRepository groupMemberRepository1,
                                           CollaboratorRepository collaboratorRepository1
    ){
        //初始化
        Notice notice = new Notice();
        notice.date = new Date();
        notice.docID = docID;
        notice.groupID = groupID;
        notice.replyID = 0;
        notice.replyID_add = 0;
        notice.userID = userID;
        notice.informerID = informerID;
        notice.status = false;
        notice.success = false;
        notice.about = 5;
        User informer = userRepository1.findUserById(informerID);
        Group group = groupRepository1.findGroupById(groupID);
        Documentation documentation = documentationRepository1.findDocumentationById(docID);
        if(informer == null  || category <= 0 || category >= 3){
            notice.msg = "Unknown error happen!";
            return notice;
        }
        if(category==1)
            notice.msg = "你在团队"+groupRepository1.findGroupById(groupID).groupName+"中的权限被"+
                    userRepository1.findUserById(informerID).username+"调整为"+permission;
        else
            notice.msg = "你在文章"+documentationRepository1.findDocumentationById(docID).title+"中的权限被"+
                    userRepository1.findUserById(informerID).username+"调整为"+permission;
        notice.success = true;
        return notice;
    }
    @GetMapping(value = "/getNotification")
    @ResponseBody
    public List<NoticeResult> getNotification(@RequestParam(value = "userId") int userID){
        List<Notice> noticeList = noticeRepository.findByUserID(userID);
        List<NoticeResult> noticeResultList = new ArrayList<>();
//        System.out.println("1");
        for(Notice notice : noticeList){
            NoticeResult noticeResult = new NoticeResult();
            noticeResult.id = notice.id;
            noticeResult.category = notice.category + ( notice.about - 1 ) * 10;
            noticeResult.date = notice.date.toString();
            noticeResult.msg = notice.msg;
            noticeResult.status = notice.status;
            if(notice.about == 3){
                Group group = groupRepository.findGroupById(notice.groupID);
                if(group != null) {
                    noticeResult.name = groupRepository.findGroupById(notice.groupID).groupName;
                    noticeResult.objectID = notice.groupID;
                }
                else{
                    notice.msg = "团队已被解散";
                }
            }
            else{
                Documentation documentation = documentationRepository.findDocumentationById(notice.docID);
                if(documentation == null)
                    continue;
                noticeResult.name = documentation.title;
                noticeResult.objectID = notice.docID;
            }
            noticeResultList.add(noticeResult);
        }
        Collections.sort(noticeResultList, (o1, o2) -> {
            int a,b;
            if(o1.status){
                a = 1;
            }
            else
                a = 0;
            if(o2.status){
                b = 1;
            }
            else
                b = 0;
            int i = a - b;
            if(i != 0){
                return i;
            }
            else{
                return o2.date.compareTo(o1.date);
            }
        });
        return noticeResultList;
    }


    @GetMapping(value = "/readNotifications")
    @ResponseBody
    public void readNotifications(@RequestParam int userID,@RequestParam int notificationID){
        Notice notice = noticeRepository.findNoticeById(notificationID);
        notice.status = true;
        noticeRepository.save(notice);
    }

}
