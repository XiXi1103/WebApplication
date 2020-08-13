package com.web.controller;

import com.web.entity.*;
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
public class CreateGroupController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @GetMapping(value = {"/createGroup"})
    @ResponseBody
    public Result create(@RequestParam("userId") int userId,@RequestParam("groupName") String groupName,
                             Model model, HttpSession session){
        int creatorId = -1;
        creatorId = userId;
        Result result = new Result();
        if(groupName == null){
            result.success = false;
            result.ID = 0 ;
            result.msg = "请填写团队名称";
            return result;
        }
        if(creatorId == -1 ){
            result.success = false;
            result.ID = 0 ;
            result.msg = "请登录";
            return result;
        }
        Group group=new Group();
        group.creatorId = creatorId;
        group.createTime = new Date();
        group.groupName = groupName;
        groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.groupId = group.id;
        groupMember.userId = creatorId;
        groupMember.permission = 5;
        groupMember.join_time = new Date();
        groupMemberRepository.save(groupMember);
        result.success = true;
        result.ID = group.id ;
        return result;
    }

}