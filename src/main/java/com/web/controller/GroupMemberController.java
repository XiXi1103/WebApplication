package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
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
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    LikesRepository likesRepository;
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @PostMapping(value = {"/addMember"})
    @ResponseBody
    public Result invite(@RequestParam int groupID,
                         @RequestParam int userID,
                         @RequestParam String username,
                         @RequestParam int permission
                        ){
        User inviter = userRepository.findUserById(userID);
        User user = userRepository.findUserByUsername(username);
        Group group = groupRepository.findGroupById(groupID);
        Result result = new Result();

        if(inviter == null){
            result.msg = "邀请者不存在或您是游客，无法邀请成员";
            result.success = false;
            return result;
        }
        else if(user == null){
            result.msg = "被邀请者不存在";
            result.success = false;
            return result;
        }
        else if(group == null){
            result.msg = "该团队不存在";
            result.success = false;
            return result;
        }
        else{
            GroupMember groupMember = new GroupMember();
            groupMember.groupId = group.id;
            groupMember.userId = user.id;
            groupMember.permission = permission;
            groupMemberRepository.save(groupMember);

            int category = 5;
            Notice notice;
            User actor = userRepository.findUserById(groupRepository.findGroupById(groupID).creatorId);
            notice = NoticeController.addNotice(user.id,actor.id,category,groupID);
            noticeRepository.save(notice);


            result.success = true;
            result.ID = group.id ;
            result.msg = "邀请成功!";
            return result;
        }


    }

    @PostMapping(value = {"/deleteMember"})
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

        int category = 6;
        Notice notice;
        User actor = userRepository.findUserById(groupRepository.findGroupById(group.id).creatorId);
        notice = NoticeController.addNotice(user.id,actor.id,category,group.id);
        noticeRepository.save(notice);

        return result;
    }

    @PostMapping(value = {"/group/modify"})
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
@GetMapping(value = {"/getGroup"})
@ResponseBody
public MyCollectionResult getGroup(@RequestParam("userID") int userId,
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
