package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

public class RecentUseController {
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

    @GetMapping(value = {"/getRecentDoc"})
    @ResponseBody
    public MyCollectionResult getRecentDoc(@RequestParam("userName") String userName, Model model, HttpSession session){
        MyCollectionResult myCollectionResult=new MyCollectionResult();
        User user=userRepository.findUserByUsername(userName);
        int l=user.recently_usednum;
        DocumentationResult documentationResult=new DocumentationResult();
        if(l>0)
            documentationResult.documentationId=user.recently_used5;
        documentationResult.documentationTitle=documentationRepository.findDocumentationById(user.recently_used5).title;
        myCollectionResult.documentationResults.add(documentationResult);
        if(l>1)
            documentationResult.documentationId=user.recently_used4;
        documentationResult.documentationTitle=documentationRepository.findDocumentationById(user.recently_used4).title;
        myCollectionResult.documentationResults.add(documentationResult);
        if(l>2)
            documentationResult.documentationId=user.recently_used3;
        documentationResult.documentationTitle=documentationRepository.findDocumentationById(user.recently_used3).title;
        myCollectionResult.documentationResults.add(documentationResult);
        if(l>3)
            documentationResult.documentationId=user.recently_used2;
        documentationResult.documentationTitle=documentationRepository.findDocumentationById(user.recently_used2).title;
        myCollectionResult.documentationResults.add(documentationResult);
        if(l>4)
            documentationResult.documentationId=user.recently_used1;
        documentationResult.documentationTitle=documentationRepository.findDocumentationById(user.recently_used1).title;
        myCollectionResult.documentationResults.add(documentationResult);
        return myCollectionResult;
    }
    @GetMapping(value = {"/removeRecentBrowsing"})
    @ResponseBody
    public RemoveRecentBrowsingResult removeRecentBrowsing(@RequestParam("userID") int userId, @RequestParam("docID") int docId,Model model, HttpSession session){
        MyCollectionResult myCollectionResult=new MyCollectionResult();
        User user=userRepository.findUserById(userId);
        int l=user.recently_usednum;
        DocumentationResult documentationResult=new DocumentationResult();
        if(docId==user.recently_used5) {
            user.recently_used5=user.recently_used4;
            user.recently_used4=user.recently_used3;
            user.recently_used3=user.recently_used2;
            user.recently_used2=user.recently_used1;
            user.recently_used1=0;
            user.recently_usednum--;
        }
        if(docId==user.recently_used4) {
            user.recently_used4=user.recently_used3;
            user.recently_used3=user.recently_used2;
            user.recently_used2=user.recently_used1;
            user.recently_used1=0;
            user.recently_usednum--;
        }
        if(docId==user.recently_used3) {
            user.recently_used3=user.recently_used2;
            user.recently_used2=user.recently_used1;
            user.recently_used1=0;
            user.recently_usednum--;
        }
        if(docId==user.recently_used2) {
            user.recently_used2=user.recently_used1;
            user.recently_used1=0;
            user.recently_usednum--;
        }
        if(docId==user.recently_used1) {
            user.recently_used1=0;
            user.recently_usednum--;
        }
        RemoveRecentBrowsingResult removeRecentBrowsingResult= new RemoveRecentBrowsingResult();
        removeRecentBrowsingResult.success=true;
        return removeRecentBrowsingResult;
    }
}
