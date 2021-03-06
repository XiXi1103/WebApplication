package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.PageList;
import com.web.entity.ReturnResult.PageListResult;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.web.controller.DocumentationRecordController.isTheSameDay;

@Controller
@CrossOrigin
public class SearchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DocumentationRepository documentationRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @GetMapping(value = {"/searchUser"})
    @ResponseBody
    public List<UserSearch> searchUser(@RequestParam String text) {
        List<User> userList = userRepository.findByUsernameLike("%" + text + "%");
        List<UserSearch> userSearchList = new ArrayList<>();

        for(User user : userList) {
            UserSearch userSearch = new UserSearch(user.id, user.username);
            userSearchList.add(userSearch);
        }
        return userSearchList;
    }

    @GetMapping(value = {"/searchDoc"})
    @ResponseBody
    public List<PageListResult> searchDoc(@RequestParam int userId, @RequestParam String text) {
        List<Documentation> documentations = documentationRepository.findDocumentationByTitleContaining(text);
        List<PageListResult> pageListResults = new ArrayList<>();
        int l = documentations.size();
        for (int i=0;i<l;i++){
            Documentation documentation=documentations.get(i);
            Date date=documentation.createTime;
            if(documentation==null)
                return pageListResults;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,date)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.date=date;
                pageList.setDates(date);
                pageList.isCreator = userId == documentation.creatorId;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }

    @GetMapping(value = {"/searchGroup"})
    @ResponseBody
    public List<PageListResult> searchGroup(@RequestParam int userId, @RequestParam String text) {
        List<Group> groups = groupRepository.findGroupByGroupNameContaining(text);
        List<PageListResult> pageListResults = new ArrayList<>();
        int l = groups.size();
        for (int i=0;i<l;i++){
            Group group=groups.get(i);
            Date date=group.createTime;
            if(group==null)
                return pageListResults;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = group.id;
                pageList.title = group.groupName;
                pageList.isCreator = userId == group.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,date)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = group.id;
                pageList.title = group.groupName;
                pageList.isCreator = userId == group.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = group.id;
                pageList.title = group.groupName;
                pageList.date=date;
                pageList.setDates(date);
                pageList.isCreator = userId == group.creatorId;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }

    /*
    @GetMapping(value = {"/searchDocThroughUsr"})
    @ResponseBody
    public List<DocSearch> searchDocThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        List<DocSearch> docSearchList = new ArrayList<>();

        List<Documentation> docList = documentationRepository.findDocumentationByCreatorId(user.id);

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                if (!doc.isTrash) {
                    DocSearch docSearch = new DocSearch(doc.title, doc.id);
                    docSearchList.add(docSearch);
                }
            }
        }

        return docSearchList;
    }
    */

    @GetMapping(value = {"/getDelDoc"})
    @ResponseBody
    public List<PageListResult> getDelDoc(@RequestParam int userID) {
        User user = userRepository.findUserById(userID);
        List<DocSearch> docSearchList = new ArrayList<>();
        List<Documentation> documentations = documentationRepository.findDocumentationByCreatorIdAndTrash(user.id);

        List<PageListResult> pageListResults=new ArrayList<>();
        int l = documentations.size();
        for (int i=0;i<l;i++){
            Documentation documentation=documentations.get(i);
            Date date=documentation.lastTime;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userID == documentation.creatorId;
                pageList.date=documentation.lastTime;
                pageList.setDates(documentation.lastTime);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,documentation.lastTime)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userID == documentation.creatorId;
                pageList.date=documentation.lastTime;
                pageList.setDates(documentation.lastTime);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.date=documentation.lastTime;
                pageList.setDates(documentation.lastTime);
                pageList.isCreator = userID == documentation.creatorId;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }

}
