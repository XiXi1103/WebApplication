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
    public List<UsrSearch> searchUser(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        List<UsrSearch> usrSearchList = new ArrayList<>();

        UsrSearch usrSearch = new UsrSearch(user.id, user.username);
        usrSearchList.add(usrSearch);

        return usrSearchList;
    }

    @GetMapping(value = {"/searchDoc"})
    @ResponseBody
    public List<DocSearch> searchDoc(@RequestParam int userId, @RequestParam String keyword) {
        List<Documentation> docList = documentationRepository.findByCreatorId(userId);
        List<DocSearch> docSearchList = new ArrayList<>();

        for (Documentation doc : docList) {
            if (doc.title.contains(keyword)) {
                DocSearch docSearch = new DocSearch(doc.title, doc.id);
                docSearchList.add(docSearch);
            }
        }

        return docSearchList;
    }

    @GetMapping(value = {"/searchGrp"})
    @ResponseBody
    public List<GrpSearch> searchGrp(@RequestParam int userId, @RequestParam String keyword) {
        List<GroupMember> grpMemberList = groupMemberRepository.findGroupMemberByUserId(userId);
        List<GrpSearch> grpSearchList = new ArrayList<>();

        for (GroupMember grpMember : grpMemberList) {
            Group grp = groupRepository.findGroupById(grpMember.groupId);
            if (grp.groupName.contains(keyword)) {
                GrpSearch grpSearch = new GrpSearch(grp.groupName, grp.id);
                grpSearchList.add(grpSearch);
            }
        }

        return grpSearchList;
    }

    @GetMapping(value = {"/searchDocThroughUsr"})
    @ResponseBody
    public List<DocSearch> searchDocThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        List<DocSearch> docSearchList = new ArrayList<>();

        List<Documentation> docList = documentationRepository.findByCreatorId(user.id);

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

    @GetMapping(value = {"/searchTrashThroughUsr"})
    @ResponseBody
    public List<DocSearch> searchTrashThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        List<DocSearch> docSearchList = new ArrayList<>();

        List<Documentation> docList = documentationRepository.findByCreatorId(user.id);

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
