package com.web.controller;

import com.web.entity.*;
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
    @PostMapping(value = {"/group/create"})
    @ResponseBody
    public Result login(@RequestBody Group_vue group_vue,
                             Model model, HttpSession session){
        String groupName=group_vue.groupName;
        int creatorId=-1;
        creatorId=group_vue.creatorId;
        String groupInformation=group_vue.groupInformation;
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
        group.creatorId=creatorId;
        group.createTime=new Date();
        group.groupName=groupName;
        group.information=groupInformation;
        groupRepository.save(group);
        result.success = true;
        result.ID = group.id ;
        return result;
    }

}