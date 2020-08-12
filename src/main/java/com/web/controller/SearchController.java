package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
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
    DocumentationRepository documentationRepository;
    GroupRepository groupRepository;

    @GetMapping(value={"/searchUser"})
    @ResponseBody
    public SearchUsrResult searchUser(@RequestParam String username) {
        List<User> userList = userRepository.findByUsername(username);
        SearchUsrResult searchUsrResult = new SearchUsrResult();

        if (!userList.isEmpty()) {
            User user = userList.get(0);
            searchUsrResult.success = true;
            searchUsrResult.usrId = user.id;
            searchUsrResult.msg = "搜索成功";
        }
        else {
            searchUsrResult.success = false;
            searchUsrResult.usrId = -1;
            searchUsrResult.msg = "搜索失败";
        };

        return searchUsrResult;
    }

    @GetMapping(value={"/searchDoc"})
    @ResponseBody
    public SearchDocResult searchDocumentation(@RequestParam String docname) {
        List<Documentation> docList = documentationRepository.findByTitle(docname);
        SearchDocResult searchDocResult = new SearchDocResult();

        return getSearchDocResult(docList, searchDocResult, true);
    }

    @PostMapping(value={"/searchGrp"})
    @ResponseBody
    public SearchGrpResult searchGroup(@RequestParam String grpname) {
        List<Group> grpList = groupRepository.findByGroupName(grpname);
        SearchGrpResult searchGrpResult = new SearchGrpResult();
        if (!grpList.isEmpty()) {
            searchGrpResult.success = true;
            for (Group grp : grpList) {
                searchGrpResult.grpIdList.add(grp.id);
            }
            searchGrpResult.msg = "搜索成功";
        }
        else  {
            searchGrpResult.success = false;
            searchGrpResult.grpIdList = null;
            searchGrpResult.msg = "搜索失败";
        }

        return searchGrpResult;
    }

    @PostMapping(value={"/searchDocThroughUsr"})
    @ResponseBody
    public SearchDocResult searchDocThroughUsr(@RequestParam String usernsame) {
        List<User> userList = userRepository.findByUsername(usernsame);
        SearchDocResult searchDocResult = new SearchDocResult();

        if (userList.isEmpty()) {
            searchDocResult.success = false;
            searchDocResult.docIdList = null;
            searchDocResult.msg = "搜索失败";
            return searchDocResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(userList.get(0).id);

        return getSearchDocResult(docList, searchDocResult, true);
    }

    @PostMapping(value={"/searchTrashThroughUsr"})
    @ResponseBody
    public SearchDocResult searchTrashThroughUsr(@RequestParam String usernsme) {
        List<User> userList = userRepository.findByUsername(usernsme);
        SearchDocResult searchDocResult = new SearchDocResult();

        if (userList.isEmpty()) {
            searchDocResult.success = false;
            searchDocResult.docIdList = null;
            searchDocResult.msg = "搜索失败";
            return searchDocResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(userList.get(0).id);

        return getSearchDocResult(docList, searchDocResult, false);
    }

    private SearchDocResult getSearchDocResult(List<Documentation> docList, SearchDocResult searchDocResult, boolean flag) {
        if (!docList.isEmpty()) {
            searchDocResult.success = true;
            if (flag) {
                for (Documentation doc : docList) {
                    if (!doc.isTrash){
                        searchDocResult.docIdList.add(doc.id);
                        searchDocResult.titleList.add(doc.title);
                    }
                }
            }
            else {
                for (Documentation doc : docList) {
                    if (doc.isTrash){
                        searchDocResult.docIdList.add(doc.id);
                        searchDocResult.titleList.add(doc.title);
                    }
                }
            }
            searchDocResult.msg = "搜索成功";
        }
        else  {
            searchDocResult.success = false;
            searchDocResult.docIdList = null;
            searchDocResult.titleList = null;
            searchDocResult.msg = "搜索失败";
        }

        return searchDocResult;
    }
}
