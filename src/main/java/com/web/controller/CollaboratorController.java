package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class CollaboratorController {
    @Autowired
    CollaboratorRepository collaboratorRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    LikesRepository likesRepository;
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @GetMapping(value = {"/addWriter"})
    @ResponseBody
    public Result addWriter(@RequestParam("userID1") int userId1,
                                        @RequestParam("userID2") int userId2,
                                        @RequestParam("docID") int docId,
                                        @RequestParam("permission") int permission,
                                       Model model, HttpSession session){
        Result result = new  Result();
        result.success=false;
        result.msg="权限不足";
        if(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userId1,docId).permission <= 4)
            return result;
//        Collaborator collaborator=new Collaborator();
//        collaborator.documentationId=docId;
//        collaborator.permission=permission;
//        collaborator.userId=userId2;
//        collaboratorRepository.save(collaborator);
//        result.success=true;

        int category = 3;
        Notice notice;
        notice = new NoticeController().addNoticeAboutDoc(userId2,userId1,category,docId,0,
                userRepository,documentationRepository,replyRepository);
        noticeRepository.save(notice);


        result.success = true;
        result.msg="发送邀请成功";
        return result;
    }
    @PostMapping(value = {"/kickCollaborator"})
    @ResponseBody
    public Result kickCollaborator(@RequestBody Collaborator_vue collaborator_vue,
                             Model model, HttpSession session){
        Result result=new Result();
        if(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId
                (collaborator_vue.userId1,collaborator_vue.docId).permission==5){
            Collaborator collaborator=collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId
                    (collaborator_vue.userId2,collaborator_vue.docId);
            collaboratorRepository.delete(collaborator);
            result.msg="踢出成功";

            int category = 4;
            Notice notice;
            notice = new NoticeController().addNoticeAboutDoc(collaborator_vue.userId2,collaborator_vue.userId2,category,collaborator.documentationId,0,
                    userRepository,documentationRepository,replyRepository);
            noticeRepository.save(notice);

            result.success=true;

        }
        else{
            result.msg="权限不足";
            result.success=false;
        }
        return result;
    }
    @PostMapping(value = {"/exitCollaborator"})
    @ResponseBody
    public Result exitCollaborator(@RequestBody Collaborator_vue collaborator_vue,
                                   Model model, HttpSession session){
        Result result=new Result();
        if(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId
                (collaborator_vue.userId1,collaborator_vue.docId)!=null){
            collaboratorRepository.delete(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId
                    (collaborator_vue.userId1,collaborator_vue.docId));
            result.msg="退出成功";

            int category = 5;
            Notice notice;
            notice = new NoticeController().addNoticeAboutDoc(collaborator_vue.userId2,collaborator_vue.userId2,category,collaborator_vue.docId,0,
                    userRepository,documentationRepository,replyRepository);
            noticeRepository.save(notice);

            result.success=true;
        }
        else{
            result.msg="未知错误";
            result.success=false;
        }
        return result;
    }
}
