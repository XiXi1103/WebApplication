package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin
@Controller
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
    @Autowired
    CollaboratorRepository collaboratorRepository;
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
    public ArrayList<PageList> getCollectionDoc(@RequestParam("userID") int userId,
                                                  Model model, HttpSession session){
        ArrayList<Collection> collections= (ArrayList<Collection>) collectionRepository.findCollectionByUserId(userId);
        int l=collections.size();
        ArrayList<PageList> pageLists=new ArrayList<>();
        PageList pageList=new PageList();
        Documentation documentation=new Documentation();
        for(int i=0;i<l;i++){
            documentation=documentationRepository.findDocumentationById(collections.get(i).id);
            pageList.id=collections.get(i).id;
            pageList.title=documentation.title;
            if(documentation.creatorId==userId)
                pageList.isCreator=true;
            else
                pageList.isCreator=false;
            pageLists.add(pageList);
        }
        return pageLists;
    }

    @GetMapping(value = {"/getMyDoc"})
    @ResponseBody
    public ArrayList<PageList> getMyDoc(@RequestParam("userID") int userId,
                                               Model model, HttpSession session){
        ArrayList<Documentation> documentations= (ArrayList<Documentation>) documentationRepository.findByCreatorId(userId);
        ArrayList<Collaborator> collaborators= collaboratorRepository.findCollaboratorByUserId(userId);
        int l1=documentations.size();
        int l2=collaborators.size();
        ArrayList<PageList> pageLists=new ArrayList<>();
        PageList pageList=new PageList();
        for (Documentation documentation : documentations) {
            pageList.id = documentation.id;
            pageList.title = documentation.title;
            pageList.isCreator = true;
            pageLists.add(pageList);
        }
        for (Collaborator collaborator : collaborators) {
            pageList.id = collaborator.id;
            pageList.title = documentationRepository.findDocumentationById(collaborator.id).title;
            pageList.isCreator = false;
            pageLists.add(pageList);
        }
        return pageLists;
    }
    @GetMapping(value = {"/getGroupDoc"})
    @ResponseBody
    public ArrayList<PageList> getGroupDoc(@RequestParam("groupID") int groupId,
                                          @RequestParam("userId") int userId,
                                          Model model, HttpSession session){
        ArrayList<Documentation> documentations= (ArrayList<Documentation>) documentationRepository.findByGroupId(groupId);
        int l1=documentations.size();
        ArrayList<PageList> pageLists=new ArrayList<>();
        PageList pageList=new PageList();
        for(int i=0;i<l1;i++){
            pageList.id=documentations.get(i).id;
            pageList.title=documentations.get(i).title;
            if(documentations.get(i).creatorId==userId)
                pageList.isCreator=true;
            else
                pageList.isCreator=false;
            pageLists.add(pageList);
        }
        return pageLists;
    }
}
