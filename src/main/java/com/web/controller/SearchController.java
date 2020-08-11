package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class SearchController {
    @Autowired
    UserRepository userRepository;
    DocumentationRepository documentationRepository;
    GroupRepository groupRepository;

    @PostMapping(value={"/searchUser"})
    @ResponseBody
    public SearchUsrResult searchUser(@RequestParam Keyword_vue keyword) {
        List<User> userList = userRepository.findByUsername(keyword.key);
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

    @PostMapping(value={"/searchDoc"})
    @ResponseBody
    public SearchDocResult searchDocumentation(@RequestParam Keyword_vue keyword) {
        List<Documentation> docList = documentationRepository.findByTitle(keyword.key);
        SearchDocResult searchDocResult = new SearchDocResult();

        if (!docList.isEmpty()) {
            searchDocResult.success = true;
            for (Documentation doc : docList) {
                searchDocResult.docIdList.add(doc.id);
            }
            searchDocResult.msg = "搜索成功";
        }
        else  {
            searchDocResult.success = false;
            searchDocResult.docIdList = null;
            searchDocResult.msg = "搜索失败";
        }

        return searchDocResult;
    }

    @PostMapping(value={"/searchGrp"})
    @ResponseBody
    public SearchGrpResult searchGroup(@RequestParam Keyword_vue keyword) {
        List<Group> grpList = groupRepository.findByGroupName(keyword.key);
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
    public SearchDocResult searchDocThroughUsr(@RequestParam Keyword_vue keyword) {
        List<User> userList = userRepository.findByUsername(keyword.key);
        SearchDocResult searchDocResult = new SearchDocResult();

        if (!userList.isEmpty()) {
            searchDocResult.success = false;
            searchDocResult.docIdList = null;
            searchDocResult.msg = "搜索失败";
            return searchDocResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(userList.get(0).id);

        if (!docList.isEmpty()) {
            searchDocResult.success = true;
            for (Documentation doc : docList) {
                searchDocResult.docIdList.add(doc.id);
            }
            searchDocResult.msg = "搜索成功";
        }
        else  {
            searchDocResult.success = false;
            searchDocResult.docIdList = null;
            searchDocResult.msg = "搜索失败";
        }

        return searchDocResult;
    }
}
