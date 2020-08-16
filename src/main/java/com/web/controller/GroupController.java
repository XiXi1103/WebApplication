package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.MemberList;
import com.web.entity.ReturnResult.Result;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.NoticeRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
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
    @Autowired
    NoticeRepository noticeRepository;
    @GetMapping(value = {"/createGroup"})
    @ResponseBody
    public Result create(@RequestParam("userId") int userId, @RequestParam("groupName") String groupName,
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

        User user = userRepository.findUserById(userId);
        Group group = groupRepository.findGroupById(groupId);

        if(user == null || group == null){
            Result result=new Result();
            result.success = false;
            result.msg="Unknown error happen!";
            return result;
        }
        if(groupRepository.findGroupById(groupId).creatorId==userId){
            Result result=new Result();
            result.success = true;
            result.ID = groupId ;
            result.msg="删除成功";
            List<GroupMember> groupMembers=  groupMemberRepository.findGroupMemberByGroupId(groupId);
            int l=groupMembers.size();
            for (GroupMember groupMember : groupMembers) {
                groupMemberRepository.delete(groupMember);
                Notice notice = new NoticeController().addNoticeAboutGroup(groupMember.userId, user.id, 4, groupId, userRepository, groupRepository);
                noticeRepository.save(notice);
            }
            return result;
        }
        else{
            Result result=new Result();
            result.success = true;
            result.ID = groupId ;
            result.msg="退出成功";
            GroupMember groupMember=  groupMemberRepository.findGroupMemberByUserIdAndGroupId(userId,groupId);
            Notice notice = new NoticeController().addNoticeAboutGroup(groupMember.userId,user.id,3,groupId,userRepository,groupRepository);
            groupMemberRepository.delete(groupMember);
            return result;
        }

    }
    @GetMapping(value = {"/catMember"})
    @ResponseBody
    public ArrayList<MemberList> catMember(@RequestParam("groupID") int groupId,
                                           Model model, HttpSession session){
        ArrayList<MemberList> memberLists=new ManagedList<>();
        MemberList memberList=new MemberList();
        List<GroupMember> groupMembers=  groupMemberRepository.findGroupMemberByGroupId(groupId);
        int l=groupMembers.size();
        for (GroupMember groupMember : groupMembers) {
            memberList.id = groupMember.userId;
            memberList.name = userRepository.findUserById(groupMember.userId).username;
            memberList.permission=groupMemberRepository.findGroupMemberByUserIdAndGroupId(memberList.id,groupId).permission;
            memberLists.add(memberList);
        }
        return memberLists;
    }
    //少了加入时的权限
    @GetMapping(value = {"/confirmGroupInvitation"})
    @ResponseBody
    public Result confirmGroupInvitation(@RequestParam("userID") int userID,
            @RequestParam("groupID") int groupID,
            @RequestParam("noticeID") int noticeID,
            @RequestParam("userResponse") boolean userResponse,
            Model model, HttpSession session){
        Result result = new Result();
        Group group = groupRepository.findGroupById(groupID);
        User user = userRepository.findUserById(userID);
        Notice notice = noticeRepository.findNoticeById(noticeID);
        if(!userResponse){
            notice.status = true;
            noticeRepository.save(notice);
            result.success = true;
        }
        else{
            notice.status = true;
            noticeRepository.save(notice);
            GroupMember groupMember = new GroupMember();
            groupMember.join_time = new Date();
            groupMember.userId = userID;
            groupMember.groupId = groupID;
            groupMemberRepository.save(groupMember);
        }
        return result;
    }
}