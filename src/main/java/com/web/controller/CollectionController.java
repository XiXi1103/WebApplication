package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;

public class CollectionController {
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

    @GetMapping(value = {"/collection"})
    @ResponseBody
    public Result collection( @RequestParam("userId") int userId,
                         @RequestParam("documentationId") int documentationId,
                         Model model, HttpSession session){
        Collection collection = collectionRepository.findCollectionByUserIdAndDocumentationId(userId,documentationId);
        if(collection==null){
            collection.collect_time=new Date();
            collection.documentationId=documentationId;
            collection.userId=userId;
            collection.status=true;
            collectionRepository.save(collection);
            Result result = new Result();
            result.success = true;
            result.ID =collection.id ;
            result.msg = "收藏成功!";
            return result;
        }
        else if(collection.status==false){
            collection.status=true;
            collection.collect_time=new Date();
            collectionRepository.save(collection);
            Result result = new Result();
            result.success = true;
            result.ID =collection.id ;
            result.msg = "收藏成功!";
            return result;
        }
        else{
            collection.status=false;
            collectionRepository.save(collection);
            Result result = new Result();
            result.success = true;
            result.ID = collection.id ;
            result.msg = "删除收藏成功!";
            return result;
        }
    }
    @GetMapping(value = {"/getCollectionDoc"})
    @ResponseBody
    public MyCollectionResult getCollectionDoc(@RequestParam("userName") String userName,
                                               Model model, HttpSession session){
        MyCollectionResult myCollectionResult=new MyCollectionResult();
        User user=userRepository.findUserByUsername(userName);
        ArrayList<Collection> collections= (ArrayList<Collection>) collectionRepository.findCollectionByUserId(user.id);
        int l=collections.size();
        DocumentationResult documentationResult=new DocumentationResult();
        for(int i=0;i<l;i++){
            documentationResult.documentationId=collections.get(i).id;
            documentationResult.documentationTitle=documentationRepository.findDocumentationById(collections.get(i).id).title;
            myCollectionResult.documentationResults.add(documentationResult);
        }
        return myCollectionResult;
    }
    @GetMapping(value = {"/getRecentDoc"})
    @ResponseBody
    public MyCollectionResult getRecentDoc(@RequestParam("userName") String userName,
                                           Model model, HttpSession session){
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
        @GetMapping(value = {"/getMyDoc"})
        @ResponseBody
        public MyCollectionResult getMyDoc(@RequestParam("userName") String userName,
                                               Model model, HttpSession session){
        MyCollectionResult myCollectionResult=new MyCollectionResult();
        User user=userRepository.findUserByUsername(userName);
        ArrayList<Documentation> documentations= (ArrayList<Documentation>) documentationRepository.findByCreatorId(user.id);
        int l=documentations.size();
        DocumentationResult documentationResult=new DocumentationResult();
        for(int i=0;i<l;i++){
            documentationResult.documentationId=documentations.get(i).id;
            documentationResult.documentationTitle=documentations.get(i).title;
            myCollectionResult.documentationResults.add(documentationResult);
        }
        return myCollectionResult;
    }
}
