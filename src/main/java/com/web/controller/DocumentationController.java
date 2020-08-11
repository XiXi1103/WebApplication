package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@CrossOrigin
@Controller
public class DocumentationController {
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;

    @PostMapping(value = {"/documentation/create"})
    @ResponseBody
    public Result create(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {

        int creatorId = -1;
        creatorId = documentation_vue.userId;
        String title = documentation_vue.title;
        Result result = new Result();
        if (title == null) {
            result.success = false;
            result.ID = 0;
            result.msg = "请填写标题";
            return result;
        }
        if (documentation_vue.content == null) {
            result.success = false;
            result.ID = 0;
            result.msg = "请填写内容";
            return result;
        }
        if (creatorId == -1) {
            result.success = false;
            result.ID = 0;
            result.msg = "请登录";
            return result;
        }

        Documentation documentation = new Documentation();
        documentation.content = documentation_vue.content;
        documentation.title = documentation_vue.title;
        documentation.createTime = new Date();
        documentation.creatorId = documentation_vue.userId;

        documentationRepository.save(documentation);

        result.success = true;
        result.ID = documentation.id;
        return result;
    }

    @PostMapping(value = {"/documentation/delete"})
    @ResponseBody
    public Result delete(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.documentationId);
        if (documentation.creatorId == documentation_vue.userId) {
            documentationRepository.delete(documentation);
            Result result = new Result();
            result.success = true;
            result.msg = "删除文档成功!";
            return result;
        } else {
            Result result = new Result();
            result.success = false;
            result.msg = "删除文档失败，权限不足!";
            return result;
        }
    }

    @PostMapping(value = {"/documentation/update"})
    @ResponseBody
    public Documentation update(@RequestBody Documentation_vue documentation_vue,
                                Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.documentationId);
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(documentation_vue.userId, documentation.groupId);
            if (groupMember.permission >= 4) {
                return documentation;
            } else {
                return null;
            }
        }
        else{
            if(documentation_vue.userId == documentation.creatorId){
                return documentation;
            }
            else if(documentation.otherPermission >= 4){
                return documentation;
            }
            else{
                return null;
            }
        }
    }
//
    @PostMapping(value = {"/documentation/show"})
    @ResponseBody
    public Documentation show(@RequestBody Documentation_vue documentation_vue,
                              Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.documentationId);
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(documentation_vue.userId, documentation.groupId);
            if (groupMember.permission >= 1) {
                return documentation;
            } else {
                return null;
            }
        }
        else{
            if(documentation_vue.userId == documentation.creatorId){
                return documentation;
            }
            else if(documentation.otherPermission >= 1){
                return documentation;
            }
            else{
                return null;
            }
        }
    }

}