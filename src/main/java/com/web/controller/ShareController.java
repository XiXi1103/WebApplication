package com.web.controller;

import com.web.entity.Documentation;
import com.web.entity.ReturnResult.PathResult;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@CrossOrigin
@Controller
public class ShareController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @GetMapping(value = {"/shareDoc"})
    @ResponseBody
    public PathResult shareDoc(@RequestParam("docId") int docId,
                                          Model model, HttpSession session){
        Documentation documentation=documentationRepository.findDocumentationById(docId);
        PathResult pathResult=new PathResult();
        pathResult.path=documentation.path;
        return pathResult;
    }
}
