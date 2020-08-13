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

    @GetMapping(value={"/search"})
    @ResponseBody
    public SearchResult search(@RequestParam int userId, @RequestParam String keyword, @RequestParam int type) {
        if (type == 0) return searchUser(keyword);
        else if (type == 1) return searchGroup(keyword);
        else if (type == 2) return searchDocumentation(keyword);
        return null;
    }

    public SearchResult searchUser(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        SearchResult searchResult = new SearchResult();

        if (user != null) {
            DocSearch docSearch = new DocSearch("搜索1", user.id);
            searchResult.resultList.add(docSearch);
        }

        return searchResult;
    }

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

    @GetMapping(value={"/searchDocThroughUsr"})
    @ResponseBody
    public SearchResult searchDocThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        SearchResult searchResult = new SearchResult();

        if (user == null) {
            searchResult.resultList = null;
            return searchResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(user.id);

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                if (!doc.isTrash) {
                    DocSearch docSearch = new DocSearch(doc.title, doc.id);
                    searchResult.resultList.add(docSearch);
                }
            }
        }

        return searchResult;
    }

    @GetMapping(value={"/searchTrashThroughUsr"})
    @ResponseBody
    public SearchResult searchTrashThroughUsr(@RequestParam String username) {
        User user = userRepository.findUserByUsername(username);
        SearchResult searchResult = new SearchResult();

        if (user == null) {
            searchResult.resultList = null;
            return searchResult;
        }

        List<Documentation> docList = documentationRepository.findByCreatorId(user.id);

        if (!docList.isEmpty()) {
            for (Documentation doc : docList) {
                if (doc.isTrash) {
                    DocSearch docSearch = new DocSearch(doc.title, doc.id);
                    searchResult.resultList.add(docSearch);
                }
            }
        }

        return searchResult;
    }

}
