package com.web.controller;

import com.web.entity.*;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;

public class GroupMemberController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @GetMapping(value = {"/addMember"})
    @ResponseBody
    public Result invite(@RequestParam("groupID") int groupId,
                         @RequestParam("userID") int userId,
                         @RequestParam("username") String userName,
                         @RequestParam("permission") int permission,
                        Model model, HttpSession session){
        User user1 = userRepository.findUserById(userId);
        User user2 = userRepository.findUserByUsername(userName);
        Group group = groupRepository.findGroupById(groupId);
        if(groupMemberRepository.findByUserIdAndGroupId(user1.id,groupId).permission<5){
            Result result = new Result();
            result.success = false;
            result.msg = "权限不足!";
            return result;
        }
        if(groupMemberRepository.findByUserIdAndGroupId(user1.id,groupId).permission<permission){
            Result result = new Result();
            result.success = false;
            result.msg = "权限不能超过邀请者!";
            return result;
        }

        GroupMember groupMember = new GroupMember();
        groupMember.groupId = group.id;
        groupMember.userId = user2.id;
        groupMember.permission = permission;
        groupMemberRepository.save(groupMember);
        Result result = new Result();
        result.success = true;
        result.ID = group.id ;
        result.msg = "邀请成功!";
        return result;
    }

    @GetMapping(value = {"/group/delete"})
    @ResponseBody
    public Result delete(@RequestBody Group_vue group_vue,
                         @RequestBody User_vue user_vue,
                         Model model, HttpSession session){
        User user = userRepository.findUserByUsername(user_vue.username);
        Group group = groupRepository.findGroupById(group_vue.groupId);
        GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(user.id,group.id);
        groupMemberRepository.delete(groupMember);
        Result result = new Result();
        result.success = true;
        result.ID = group.id ;
        result.msg = "删除成员成功!";
        return result;
    }

    @PostMapping(value = {"/modifyPermission"})
    @ResponseBody
    public Result modifyPermission(@RequestBody Group_vue group_vue,
                               @RequestBody User_vue user_vue,
                             @RequestParam int permission,
                             Model model, HttpSession session){
        User user = userRepository.findUserByUsername(user_vue.username);
        Group group = groupRepository.findGroupById(group_vue.groupId);
        GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(user.id,group.id);

        Result result = new Result();
        if(permission < 0 || permission > 5){
            result.success = false;
//            result.ID = group.id ;
            result.msg = "未知错误!";
            return result;
        }
        groupMember.permission = permission;
        groupMemberRepository.save(groupMember);

        result.success = true;
//        result.ID = group.id ;
        result.msg = "修改权限成功!";
        return result;
    }
//    0 都没有，1 查看，2 评论，3 分享，4 修改,5 创建者
@GetMapping(value = {"/getJoinGroup"})
@ResponseBody
public MyCollectionResult getJoinGroup(@RequestParam("userID") int userId,
                                      Model model, HttpSession session){
    MyCollectionResult myCollectionResult=new MyCollectionResult();
    ArrayList<GroupMember> groupMembers= (ArrayList<GroupMember>) groupMemberRepository.findGroupMemberByUserId(userId);
    int l=groupMembers.size();
    DocumentationResult documentationResult=new DocumentationResult();
    for(int i=0;i<l;i++){
        documentationResult.documentationId=groupMembers.get(i).groupId;
        documentationResult.documentationTitle=groupRepository.findGroupById(groupMembers.get(i).groupId).groupName;
        myCollectionResult.documentationResults.add(documentationResult);
    }
    return myCollectionResult;
}
}
