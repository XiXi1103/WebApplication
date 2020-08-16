package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.DocumentationResult;
import com.web.entity.ReturnResult.PageList;
import com.web.entity.ReturnResult.RemoveRecentBrowsingResult;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@CrossOrigin
@Controller
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
        PageList pageList1=new PageList();
        PageList pageList2=new PageList();
        PageList pageList3=new PageList();
        PageList pageList4=new PageList();
        PageList pageList5=new PageList();
        User user=userRepository.findUserById(userId);
        int l=user.recently_usednum;

        if(l>0){
            pageList5.id=user.recently_used5;
            pageList5.title=documentationRepository.findDocumentationById(user.recently_used5).title;
            pageList5.isCreator= documentationRepository.findDocumentationById(user.recently_used5).creatorId == userId;
            pageLists.add(pageList5);
        }

        if(l>1){
            pageList4.id=user.recently_used4;
            pageList4.title=documentationRepository.findDocumentationById(user.recently_used4).title;
            pageList4.isCreator= documentationRepository.findDocumentationById(user.recently_used4).creatorId == userId;
            pageLists.add(pageList4);
        }
        if(l>2){
            pageList3.id=user.recently_used3;
            pageList3.title=documentationRepository.findDocumentationById(user.recently_used3).title;
            pageList3.isCreator= documentationRepository.findDocumentationById(user.recently_used3).creatorId == userId;
            pageLists.add(pageList3);
        }
        if(l>3){
            pageList2.id=user.recently_used2;
            pageList2.title=documentationRepository.findDocumentationById(user.recently_used2).title;
            pageList2.isCreator= documentationRepository.findDocumentationById(user.recently_used2).creatorId == userId;
            pageLists.add(pageList2);
        }
        if(l>4){

            pageList1.id=user.recently_used1;
            pageList1.title=documentationRepository.findDocumentationById(user.recently_used1).title;
            pageList1.isCreator= documentationRepository.findDocumentationById(user.recently_used1).creatorId == userId;
            pageLists.add(pageList1);
        }
        return pageLists;
    }
    @GetMapping(value = {"/removeRecentBrowsing"})
    @ResponseBody
    public RemoveRecentBrowsingResult removeRecentBrowsing(@RequestParam("userID") int userId, @RequestParam("docID") int docId, Model model, HttpSession session){
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
