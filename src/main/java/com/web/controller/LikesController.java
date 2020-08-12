package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.LikesRepository;
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
public class LikesController {
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    LikesRepository likesRepository;
    @Autowired
    DocumentationRepository documentationRepository;
    @PostMapping(value = {"/likes"})
    @ResponseBody
    public Result likes(@RequestBody Likes_vue likes_vue,
                        Model model, HttpSession session){
        Likes likes = new Likes();
        Result result = new Result();
        if(likes_vue.isDoc && documentationRepository.findDocumentationById(likes_vue.docId) == null){
            result.success = false;
            result.msg = "文档不存在";
            result.ID = 0;
            return result;
        }
        if(!likes_vue.isDoc && replyRepository.findReplyById(likes_vue.replyId) == null){
            result.success = false;
            result.msg = "评论不存在";
            result.ID = 0;
            return result;
        }
        if(likes_vue.userId <= 0){
            result.success = false;
            result.msg = "请登录";
            result.ID = -1;
            return result;
        }
        Likes tmp = likesRepository.findByDocIdAndReplyId(likes_vue.docId,likes_vue.replyId);
        if(tmp != null){
            tmp.status = false;
            likesRepository.save(tmp);
            result.success = true;
            result.msg = "取消点赞成功";
            result.ID = tmp.id;
        }
        else {
            likes.userId = likes_vue.userId;
            likes.date = new Date();
            likes.docId = likes_vue.docId;
            likes.isDoc = likes_vue.isDoc;
            likes.replyId = likes_vue.replyId;
            likesRepository.save(likes);

            result.success = true;
            result.msg = "点赞成功";
            result.ID = likes.id;
        }
        return result;
    }


}
