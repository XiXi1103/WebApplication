package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.ReplyRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @PostMapping(value = {"/reply"})
    @ResponseBody
    public Result reply(@RequestBody Reply_vue reply_vue,
                             Model model, HttpSession session){
        Reply reply = new Reply();
        Result result = new Result();
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
        reply.date = new Date();
        reply.docId = reply_vue.docId;
        reply.isReply = reply_vue.isReply;
        reply.replyId = reply_vue.replyId;
        replyRepository.save(reply);

        result.success = true;
        result.msg = "评论成功";
        result.ID = reply.id;
        return result;
    }

    @PostMapping(value = {"/reply/delete"})
    @ResponseBody
    public Result delete(@RequestBody Reply_vue reply_vue,
                        Model model, HttpSession session){
        Reply reply = replyRepository.findReplyById(reply_vue.id);
        List<Reply> replyList = replyRepository.findByReplyId(reply.id);
        replyRepository.delete(reply);
        for(Reply reply1:replyList){
            replyRepository.delete(reply1);
        }
        Result result = new Result();
        result.success = true;
        result.msg = "删除评论成功";
        result.ID = reply.id;
        return result;
    }
}
