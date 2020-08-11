package com.web.controller;

import com.web.entity.*;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

public class GroupMemberController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @PostMapping(value = {"/group/invite"})
    @ResponseBody
    public Result invite(@RequestBody Group_vue group_vue,
                        @RequestBody User_vue user_vue,
                        Model model, HttpSession session){
        User user = userRepository.findByUsername(user_vue.username).get(0);
        Group group = groupRepository.findGroupById(group_vue.groupId);
        GroupMember groupMember = new GroupMember();
        groupMember.groupId = group.id;
        groupMember.userId = user.id;
        groupMember.permission = 1;
        groupMemberRepository.save(groupMember);
        Result result = new Result();
        result.success = true;
        result.ID = group.id ;
        result.msg = "邀请成功!";
        return result;
    }

    @PostMapping(value = {"/group/delete"})
    @ResponseBody
    public Result delete(@RequestBody Group_vue group_vue,
                         @RequestBody User_vue user_vue,
                         Model model, HttpSession session){
        User user = userRepository.findByUsername(user_vue.username).get(0);
        Group group = groupRepository.findGroupById(group_vue.groupId);
        GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(user.id,group.id);
        groupMemberRepository.delete(groupMember);
        Result result = new Result();
        result.success = true;
        result.ID = group.id ;
        result.msg = "删除成员成功!";
        return result;
    }

    @PostMapping(value = {"/group/delete"})
    @ResponseBody
    public Result modifyPermission(@RequestBody Group_vue group_vue,
                               @RequestBody User_vue user_vue,
                             @RequestParam int permission,
                             Model model, HttpSession session){
        User user = userRepository.findByUsername(user_vue.username).get(0);
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
}
