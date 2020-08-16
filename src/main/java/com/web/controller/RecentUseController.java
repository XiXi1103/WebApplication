package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

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
    public ArrayList<PageList> getRecentDoc(@RequestParam("userID") int userId, Model model, HttpSession session){
        ArrayList<PageList> pageLists=new ArrayList<>();
        PageList pageList=new PageList();
        User user=userRepository.findUserById(userId);
        int l=user.recently_usednum;

        if(l>0){
            pageList.id=user.recently_used5;
            pageList.title=documentationRepository.findDocumentationById(user.recently_used5).title;
            pageList.isCreator= documentationRepository.findDocumentationById(user.recently_used5).creatorId == userId;
            pageLists.add(pageList);
        }

        if(l>1){
            pageList.id=user.recently_used4;
            pageList.title=documentationRepository.findDocumentationById(user.recently_used4).title;
            pageList.isCreator= documentationRepository.findDocumentationById(user.recently_used4).creatorId == userId;
            pageLists.add(pageList);
        }
        if(l>2){
            pageList.id=user.recently_used3;
            pageList.title=documentationRepository.findDocumentationById(user.recently_used3).title;
            pageList.isCreator= documentationRepository.findDocumentationById(user.recently_used3).creatorId == userId;
            pageLists.add(pageList);
        }
        if(l>3){
            pageList.id=user.recently_used2;
            pageList.title=documentationRepository.findDocumentationById(user.recently_used2).title;
            pageList.isCreator= documentationRepository.findDocumentationById(user.recently_used2).creatorId == userId;
            pageLists.add(pageList);
        }
        if(l>4){
            pageList.id=user.recently_used1;
            pageList.title=documentationRepository.findDocumentationById(user.recently_used1).title;
            pageList.isCreator= documentationRepository.findDocumentationById(user.recently_used1).creatorId == userId;
            pageLists.add(pageList);
        }
        return pageLists;
    }
    @GetMapping(value = {"/removeRecentBrowsing"})
    @ResponseBody
    public RemoveRecentBrowsingResult removeRecentBrowsing(@RequestParam("userID") int userId, @RequestParam("docID") int docId,Model model, HttpSession session){
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
