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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@Controller
public class GroupController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
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
    @GetMapping(value = {"/delGroup"})
    @ResponseBody
    public Result deleteOrExit(@RequestParam("userId") int userId,@RequestParam("groupId") int groupId,
                         Model model, HttpSession session){
        if(groupRepository.findGroupById(groupId).creatorId==userId){
            Result result=new Result();
            result.success = true;
            result.ID = groupId ;
            result.msg="删除成功";
            List<GroupMember> groupMembers=  groupMemberRepository.findGroupMemberByGroupId(groupId);
            int l=groupMembers.size();
            for(int i=0;i<l;i++){
                groupMemberRepository.delete(groupMembers.get(i));
            }
            return result;
        }
        else{
            Result result=new Result();
            result.success = true;
            result.ID = groupId ;
            result.msg="退出成功";
            GroupMember groupMember=  groupMemberRepository.findGroupMemberByUserIdAndGroupId(userId,groupId);
            groupMemberRepository.delete(groupMember);
            return result;
        }

    }
    @GetMapping(value = {"/catMember"})
    @ResponseBody
    public MyCollectionResult catMember(@RequestParam("groupId") int groupId,
                               Model model, HttpSession session){
        MyCollectionResult myCollectionResult=new MyCollectionResult();
        List<GroupMember> groupMembers=  groupMemberRepository.findGroupMemberByGroupId(groupId);
        int l=groupMembers.size();
        DocumentationResult documentationResult=new DocumentationResult();
        for(int i=0;i<l;i++){
            documentationResult.documentationId=groupMembers.get(i).userId;
            documentationResult.documentationTitle= userRepository.findUserById(groupMembers.get(i).userId).username;
            documentationResult.isCreator=true;
            myCollectionResult.documentationResults.add(documentationResult);
        }
        return myCollectionResult;
    }
}