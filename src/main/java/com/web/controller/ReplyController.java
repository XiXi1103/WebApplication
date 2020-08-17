package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.ReplyResult;
import com.web.entity.ReturnResult.Result;
import com.web.entity.vue.Reply_vue;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@CrossOrigin
@Controller
public class ReplyController {
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    GroupRepository groupRepository;
    @PostMapping(value = {"/reply"})
    @ResponseBody
    public Result reply(@RequestBody Reply_vue reply_vue,
                        Model model, HttpSession session){
        Reply reply = new Reply();
        Result result = new Result();
        System.out.println(reply_vue.docId);
        if(documentationRepository.findDocumentationById(reply_vue.docId) == null){
            result.success = false;
            result.msg = "文档不存在";
            result.ID = 0;
            return result;
        }
        if(reply_vue.isReply && replyRepository.findReplyById(reply_vue.replyId) == null){
            result.success = false;
            result.msg = "评论不存在";
            result.ID = 0;
            return result;
        }
        if(reply_vue.content == null){
            result.success = false;
            result.msg = "评论不能为空";
            result.ID = 0;
            return result;
        }
        if(reply_vue.userId <= 0){
            result.success = false;
            result.msg = "请登录";
            result.ID = -1;
            return result;
        }
        reply.content = reply_vue.content;
        reply.likes = 0;
        reply.userId = reply_vue.userId;
        reply.username = userRepository.findUserById(reply_vue.userId).username;
        reply.date = new Date();
        reply.docId = reply_vue.docId;
        reply.isReply = reply_vue.isReply;
        reply.replyId = reply_vue.replyId;
        replyRepository.save(reply);

        int category = 1;
        Notice notice;
        User author = userRepository.findUserById(documentationRepository.findDocumentationById(reply.docId).creatorId);
        if(reply.isReply){
            Reply reply1 = replyRepository.findReplyById(reply_vue.replyId);
            notice = new NoticeController().addNoticeAboutReply(author.id,reply.userId,category,reply.docId,reply.id,
                    userRepository,documentationRepository,replyRepository);
        }
        else {

            notice = new NoticeController().addNoticeAboutDoc(author.id,reply.userId,category,reply.replyId,reply.id,
                    userRepository,documentationRepository,replyRepository);
        }
        noticeRepository.save(notice);

        result.success = true;
        result.msg = "评论成功";
        result.ID = reply.id;
        return result;
    }

    @GetMapping(value = {"/deleteReply"})
    @ResponseBody
    public Result delete(@RequestParam int replyID,
                        Model model, HttpSession session){
        Reply reply = replyRepository.findReplyById(replyID);
        List<Reply> replyList = replyRepository.findByReplyId(reply.id);
        replyRepository.delete(reply);
        for(Reply reply1:replyList){
            replyRepository.delete(reply1);
        }
        Result result = new Result();
        result.success = true;
        result.msg = "删除成功";
        result.ID = reply.id;
        return result;
    }

    @GetMapping(value = {"/findAllReply"})
    @ResponseBody
    public ReplyResult reply(@RequestParam int docId,
                             Model model, HttpSession session){
        ReplyResult replyResult = new ReplyResult();
        if(docId == 0){
            replyResult.success = false;
            replyResult.msg = "文档不存在！";
            return replyResult;
        }
        List<Reply> replyList = replyRepository.findByDocId(docId);
        replyResult.success = true;
        replyResult.replyList = replyList;
        return replyResult;
    }
}
