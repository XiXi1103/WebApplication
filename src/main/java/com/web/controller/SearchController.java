package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping(value = {"/searchUser"})
    @ResponseBody
    public List<UserSearch> searchUser(@RequestParam String username) {
        List<User> userList = userRepository.findByUsernameLike("%" + username + "%");
        List<UserSearch> userSearchList = new ArrayList<>();

        for(User user : userList) {
            UserSearch userSearch = new UserSearch(user.id, user.username);
            userSearchList.add(userSearch);
        }
        return userSearchList;
    }

    @GetMapping(value = {"/searchDoc"})
    @ResponseBody
    public List<DocSearch> searchDoc(@RequestParam int userId, @RequestParam String text) {
        List<Documentation> docList = documentationRepository.findDocumentationByCreatorId(userId);
        List<DocSearch> docSearchList = new ArrayList<>();

        for (Documentation doc : docList) {
            if (doc.title.toLowerCase().contains(text.toLowerCase())) {
                DocSearch docSearch = new DocSearch(doc.title, doc.id);
                docSearchList.add(docSearch);
            }
        }

        return docSearchList;
    }

    @GetMapping(value = {"/searchGroup"})
    @ResponseBody
    public List<GrpSearch> searchGroup(@RequestParam int userId, @RequestParam String text) {
        List<GroupMember> grpMemberList = groupMemberRepository.findGroupMemberByUserId(userId);
        List<GrpSearch> grpSearchList = new ArrayList<>();

        for (GroupMember grpMember : grpMemberList) {
            Group grp = groupRepository.findGroupById(grpMember.groupId);
            if (grp.groupName.toLowerCase().contains(text.toLowerCase())) {
                GrpSearch grpSearch = new GrpSearch(grp.groupName, grp.id);
                grpSearchList.add(grpSearch);
            }
        }

        return grpSearchList;
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
    public List<DocSearch> getDelDoc(@RequestParam int userId) {
        User user = userRepository.findUserById(userId);
        List<DocSearch> docSearchList = new ArrayList<>();

        List<Documentation> docList = documentationRepository.findDocumentationByCreatorId(user.id);

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                if (doc.isTrash) {
                    DocSearch docSearch = new DocSearch(doc.title, doc.id);
                    docSearchList.add(docSearch);
                }
            }
        }

        return docSearchList;
    }

}
