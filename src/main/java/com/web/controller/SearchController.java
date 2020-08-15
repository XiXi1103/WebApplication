package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = {"/search"})
    @ResponseBody
    public SearchResult search(@RequestParam int userId, @RequestParam String keyword) {
        SearchResult searchResult = new SearchResult();
        List<Documentation> docList = documentationRepository.findByCreatorId(userId);

        for (Documentation doc : docList) {
            if (doc.title.contains(keyword)) {
                DocSearch docSearch = new DocSearch(doc.title, doc.id);
                searchResult.docResultList.add(docSearch);
            }
        }

        List<GroupMember> grpMemberList = groupMemberRepository.findGroupMemberByUserId(userId);

        for (GroupMember grpMember : grpMemberList) {
            Group grp = groupRepository.findGroupById(grpMember.groupId);
            if (grp.groupName.contains(keyword)) {
                GrpSearch grpSearch = new GrpSearch(grp.groupName, grp.id);
                searchResult.grpResultList.add(grpSearch);
            }
        }

        return searchResult;
    }

    @GetMapping(value = {"/searchDocThroughUsr"})
    @ResponseBody
    public MyDocResult searchDocThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        MyDocResult myDocResult = new MyDocResult();

        if (user == null) {
            myDocResult.docResultList = null;
            return myDocResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(user.id);

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                if (!doc.isTrash) {
                    DocSearch docSearch = new DocSearch(doc.title, doc.id);
                    myDocResult.docResultList.add(docSearch);
                }
            }
        }

        return myDocResult;
    }

    @GetMapping(value = {"/searchTrashThroughUsr"})
    @ResponseBody
    public MyDocResult searchTrashThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        MyDocResult myDocResult = new MyDocResult();

        if (user == null) {
            myDocResult.docResultList = null;
            return myDocResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(user.id);

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                if (doc.isTrash) {
                    DocSearch docSearch = new DocSearch(doc.title, doc.id);
                    myDocResult.docResultList.add(docSearch);
                }
            }
        }

        return myDocResult;
    }

    @GetMapping(value = {"/searchUser"})
    @ResponseBody
    public UserResult searchUser(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        UserResult userResult = new UserResult();

        UsrSearch usrSearch = new UsrSearch(user.id, user.username);
        userResult.userList.add(usrSearch);

        return userResult;
    }

    /*
    public SearchResult searchGroup(String grpname) {
        List<Group> grpList = groupRepository.findByGroupName(grpname);
        SearchResult searchResult = new SearchResult();

        if (!grpList.isEmpty()) {
            for (Group grp : grpList) {
                DocSearch docSearch = new DocSearch(grp.groupName, grp.id);
                searchResult.resultList.add(docSearch);
            }
        }

        return searchResult;
    }

    public SearchResult searchDocumentation(String docname) {
        List<Documentation> docList = documentationRepository.findByTitle(docname);
        SearchResult searchResult = new SearchResult();

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                DocSearch docSearch = new DocSearch(doc.title, doc.id);
                searchResult.resultList.add(docSearch);
            }
        }

        return searchResult;
    }

     */
}
