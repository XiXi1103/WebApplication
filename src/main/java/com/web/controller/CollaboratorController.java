package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.Result;
import com.web.entity.ReturnResult.WriterList;
import com.web.entity.vue.Collaborator_vue;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
// 严：传入参数检查完毕（id是否存在），除两个返回list的未检查
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
                            Model model, HttpSession session){
        Result result = new Result();
        result.success=false;
        result.ID = 0;
        result.result = null;
        result.msg="权限不足";

        if( !CheckController.checkUserById(userId1)){
            result.msg = "当前用户不存在或是游客";
            return result;
        }
        if( !CheckController.checkUserByUsername(userName)){
            result.msg = "查询不到被邀请者";
            return result;
        }
        if( !CheckController.checkDocById(docId)){
            result.msg = "对应文档不存在";
            return result;
        }

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
        if(noticeRepository.findNoticeByUserIDAndGroupIDAndCategory(user.id,docId,3)!=null){
            result.success = false;
            result.ID = docId;
            result.msg = "已邀请!";
            return result;
        }
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
        //判断输入
        if(!CheckController.checkUserById(collaborator_vue.userId1)){
            result.success = false;
            result.msg = "发起者不存在";
            return result;
        }
        if(!CheckController.checkUserById(collaborator_vue.userId2)){
            result.success = false;
            result.msg = "被踢者不存在";
            return result;
        }
        if(!CheckController.checkDocById(collaborator_vue.docId)){
            result.success = false;
            result.msg = "该文档不存在";
            return result;
        }

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
        if(!CheckController.checkUserById(collaborator_vue.userId1)){
            result.success = false;
            result.msg = "发起者不存在";
            return result;
        }
        if(!CheckController.checkDocById(collaborator_vue.docId)){
            result.success = false;
            result.msg = "该文档不存在";
            return result;
        }
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
//        if(!CheckController.checkDocById(docId)){
//            result.success = false;
//            result.msg = "该文档不存在";
//            return result;
//        }
        ArrayList<Collaborator> collaborators= (ArrayList<Collaborator>) collaboratorRepository.
                findCollaboratorByDocumentationId(docId);
        int l=collaborators.size();
        ArrayList<WriterList> writerLists=new ArrayList<>();
        for(int i=0;i<l;i++){
            WriterList writerList=new WriterList();
            writerList.id=collaborators.get(i).userId;
            writerList.name=userRepository.findUserById(writerList.id).username;
            writerList.permission=collaborators.get(i).permission;
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
        if(!CheckController.checkUserById(userId)){
            result.success = false;
            result.msg = "邀请者不存在";
            return result;
        }
        if(!CheckController.checkDocById(docId)){
            result.success = false;
            result.msg = "该文档已不存在";
            return result;
        }
        if(!CheckController.checkNoticeById(noticeId)){
            result.success = false;
            result.msg = "该邀请已不存在";
            return result;
        }
        Documentation documentation=documentationRepository.findDocumentationById(docId);
        User user = userRepository.findUserById(userId);
        Notice notice = noticeRepository.findNoticeById(noticeId);

        if(!userResponse){
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
    @GetMapping(value = {"/writerPermission"})
    @ResponseBody
    public Result writerPermission(@RequestParam(value = "userID") int userId,
                                   @RequestParam String username,
                                   @RequestParam(value = "docID") int docId,
                                   @RequestParam int permission,
                                   Model model, HttpSession session){
        Result result = new Result();
        if(!CheckController.checkUserById(userId)){
            result.success = false;
            result.msg = "发起用户不存在";
            return result;
        }
        if(!CheckController.checkUserByUsername(username)){
            result.success = false;
            result.msg = "被修改权限用户不存在";
            return result;
        }
        if(!CheckController.checkDocById(docId)){
            result.success = false;
            result.msg = "该文档已不存在";
            return result;
        }
        Documentation documentation=documentationRepository.findDocumentationById(docId);
        User user=userRepository.findUserByUsername(username);
        Collaborator collaborator=collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userId,docId);

        if(userId==documentation.creatorId||collaborator.permission==5){
            Collaborator collaborator1=collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(user.id,docId);
            collaborator1.permission=permission;
            collaboratorRepository.save(collaborator1);
            result = new Result();
            result.success = true;
            result.ID = docId ;
            result.msg = "修改权限成功!";
            int category = 2;
            Notice notice;
            notice = new NoticeController().addNoticeAboutPermission(user.id,userId,category,0,docId,permission,
                    userRepository,documentationRepository,groupRepository,groupMemberRepository,collaboratorRepository);
            noticeRepository.save(notice);
        }
        else{
            result = new Result();
            result.success = false;
            result.ID = docId ;
            result.msg = "权限不足!";
        }
        return result;
    }
}
