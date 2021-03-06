package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.GroupList;
import com.web.entity.ReturnResult.Result;
import com.web.entity.vue.Group_vue;
import com.web.entity.vue.User_vue;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
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
    @Autowired
    CollaboratorRepository collaboratorRepository;
    @GetMapping(value = {"/addMember"})
    @ResponseBody
    public Result invite(@RequestParam int groupID,
                         @RequestParam int userID,
                         @RequestParam String username
                        ){
//        System.out.println("0");
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
//            GroupMember groupMember = new GroupMember();
//            groupMember.groupId = group.id;
//            groupMember.userId = user.id;
//            groupMember.permission = permission;
//            groupMemberRepository.save(groupMember);

            int category = 1;
            Notice notice;
            User actor = userRepository.findUserById(groupRepository.findGroupById(groupID).creatorId);
            if(noticeRepository.findNoticeByUserIDAndGroupIDAndCategory(user.id,groupID,1)!=null){
                result.success = false;
                result.ID = group.id ;
                result.msg = "已邀请!";
                return result;
            }
            notice = new NoticeController().addNoticeAboutGroup(user.id,actor.id,category,groupID,
                    userRepository,groupRepository);
            noticeRepository.save(notice);
            result.success = true;
            result.ID = group.id ;
            result.msg = "邀请成功!";
            return result;
        }


    }

    @GetMapping(value = {"/kickMember"})
    @ResponseBody
    public Result delete(@RequestParam int userId1,
                         @RequestParam int userId2,
                         @RequestParam int groupId,
                         Model model, HttpSession session){
        User informer = userRepository.findUserById(userId1);
        User user = userRepository.findUserById(userId2);
        Group group = groupRepository.findGroupById(groupId);
        if(informer == null){
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "当前用户不存在!";
            return result;
        }
        if(user == null){
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "被删除用户不存在!";
            return result;
        }
        if(group == null){
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "该团队不存在!";
            return result;
        }
        GroupMember groupMember1 = groupMemberRepository.findGroupMemberByUserIdAndGroupId(userId1,groupId);
        GroupMember groupMember2 = groupMemberRepository.findGroupMemberByUserIdAndGroupId(userId2,groupId);
        if(groupMember1 == null){
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "您已不在该团队中";
            return result;
        }
        if(groupMember2 == null){
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "该用户已不在该团队中";
            return result;
        }
        if(groupMember1.permission==5){
            groupMemberRepository.delete(groupMember2);
            Result result = new Result();
            result.success = true;
            result.ID = group.id ;
            result.msg = "删除成员成功!";
            int category = 2;
            Notice notice;
            notice = new NoticeController().addNoticeAboutGroup(userId2,userId1,category,group.id,
                    userRepository,groupRepository);
            noticeRepository.save(notice);
            return result;
        }
        else{
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "权限不足!";
            return result;
        }
    }

    @GetMapping(value = {"/changePermission"})
    @ResponseBody
    public Result changePermission(@RequestParam int userID1,
                                   @RequestParam int permission,
                                   @RequestParam int groupID,
                                   @RequestParam int userID2,
                         Model model, HttpSession session){
        Group group = groupRepository.findGroupById(groupID);
        GroupMember groupMember1 = groupMemberRepository.findGroupMemberByUserIdAndGroupId(userID1,groupID);
        GroupMember groupMember2 = groupMemberRepository.findGroupMemberByUserIdAndGroupId(userID2,groupID);
        if(groupMember1.permission==5){
            groupMember2.permission=permission;
            groupMemberRepository.save(groupMember2);
            Result result = new Result();
            result.success = true;
            result.ID = group.id ;
            result.msg = "修改权限成功!";
            int category = 1;
            Notice notice;
            notice = new NoticeController().addNoticeAboutPermission(userID2,userID1,category,group.id,0,permission,
                    userRepository,documentationRepository,groupRepository,groupMemberRepository,collaboratorRepository);
            noticeRepository.save(notice);
            return result;
        }
        else{
            Result result = new Result();
            result.success = false;
            result.ID = group.id ;
            result.msg = "权限不足!";
            return result;
        }
    }


//    0 都没有，1 查看，2 评论，3 分享，4 修改,5 创建者
@GetMapping(value = {"/getJoinGroup"})
@ResponseBody
public List<GroupList> getGroup(@RequestParam int userID,
                                     Model model, HttpSession session){
//        System.out.println(userID);
    List<GroupList> groupLists =new ArrayList<>();
    List<GroupMember> groupMembers= groupMemberRepository.findGroupMemberByUserId(userID);
    int l = groupMembers.size();
    for(int i = 0 ; i < l; i++){
        GroupList groupList = new GroupList();
        groupList.id = groupMembers.get(i).groupId;
        groupList.name = groupRepository.findGroupById(groupMembers.get(i).groupId).groupName;
        groupList.isCreator = groupRepository.findGroupById(groupMembers.get(i).groupId).creatorId == userID;
        groupLists.add(i,groupList);
//        System.out.println(groupList1.get(i).name);
    }
//    System.out.println(groupList1);
    return groupLists;
}

}
