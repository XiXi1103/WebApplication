package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin
@Controller
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
                                        @RequestParam("username") String userName,
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
        User user = userRepository.findUserByUsername(userName);
        int category = 3;
        Notice notice;
        notice = new NoticeController().addNoticeAboutDoc(user.id,userId1,category,docId,0,
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
            notice = new NoticeController().addNoticeAboutDoc(collaborator_vue.userId2,
                    collaborator_vue.userId1,category,collaborator.documentationId,0,
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
    @GetMapping(value = {"/catWriter"})
    @ResponseBody
    public ArrayList<WriterList> catWriter(@RequestParam("docID") int docId,
                                           Model model, HttpSession session){
        ArrayList<Collaborator> collaborators= (ArrayList<Collaborator>) collaboratorRepository.
                findCollaboratorByDocumentationId(docId);
        int l=collaborators.size();
        ArrayList<WriterList> writerLists=new ArrayList<>();
        WriterList writerList=new WriterList();
        for(int i=0;i<l;i++){
            writerList.id=collaborators.get(i).userId;
            writerList.name=userRepository.findUserById(writerList.id).username;
            writerLists.add(writerList);
        }
        return writerLists;
    }
    @GetMapping(value = {"/confirmDocInvitation"})
    @ResponseBody
    public Result confirmDocInvitation(@RequestParam("userID") int userId,
                                       @RequestParam("docID") int docId,
                                       @RequestParam("userResponse") boolean userResponse,
                                       @RequestParam("noticeID") int noticeId,
                                         Model model, HttpSession session){
        Result result = new Result();
        Documentation documentation=documentationRepository.findDocumentationById(docId);
        User user = userRepository.findUserById(userId);
        Notice notice = noticeRepository.findNoticeById(noticeId);

        if(userResponse == false){
            notice.status = true;
            noticeRepository.save(notice);
            result.success = true;
        }
        else{
            notice.status = true;
            noticeRepository.save(notice);
            Collaborator collaborator=new Collaborator();
            collaborator.permission=4;
            collaborator.userId=userId;
            collaborator.documentationId=docId;
            collaboratorRepository.save(collaborator);
        }
        return result;
    }
}
