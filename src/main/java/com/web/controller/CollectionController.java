package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.PageList;
import com.web.entity.ReturnResult.Result;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public Result collection(@RequestParam int userId,
                             @RequestParam int docId,
                             Model model, HttpSession session){
        Collection collection = collectionRepository.findCollectionByUserIdAndDocumentationId(userId,docId);
        if(collection==null){
            collection = new Collection();
            collection.collect_time=new Date();
            collection.documentationId=docId;
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

        for(int i=0;i<l;i++){

            Documentation documentation=new Documentation();
            documentation=documentationRepository.findDocumentationById(collections.get(i).id);
            if(documentation.isTemplate)
                continue;
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
    public List<PageList> getMyDoc(@RequestParam int userID,
                                               Model model, HttpSession session){
        List<Documentation> documentations= documentationRepository.findDocumentationByCreatorId(userID);
        List<Collaborator> collaborators= collaboratorRepository.findCollaboratorByUserId(userID);
        int l1=documentations.size();
        int l2=collaborators.size();
        List<PageList> pageLists=new ArrayList<>();

        for (Documentation documentation : documentations) {
            PageList pageList=new PageList();
            pageList.id = documentation.id;
            pageList.title = documentation.title;
            pageList.isCreator = true;
            pageLists.add(pageList);
        }
        for (Collaborator collaborator : collaborators) {
            PageList pageList=new PageList();
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
        ArrayList<Documentation> documentations= (ArrayList<Documentation>) documentationRepository.findDocumentationByGroupId(groupId);
        int l1=documentations.size();
        ArrayList<PageList> pageLists=new ArrayList<>();

        for(int i=0;i<l1;i++){
            PageList pageList=new PageList();
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
    @GetMapping(value = {"/getAllTemplate"})
    @ResponseBody
    public ArrayList<PageList> getAllTemplate(Model model, HttpSession session){
        ArrayList<Documentation> documentations= (ArrayList<Documentation>) documentationRepository.findDocumentationByisTemplate(true);
        int l1=documentations.size();
        ArrayList<PageList> pageLists=new ArrayList<>();
        for(int i=0;i<l1;i++){
            PageList pageList=new PageList();
            pageList.id=documentations.get(i).id;
            pageList.title=documentations.get(i).title;
            pageLists.add(pageList);
        }
        return pageLists;
    }
    @GetMapping(value = {"/getMyTemplate1"})
    @ResponseBody
    public ArrayList<PageList> getMyTemplate(@RequestParam("userId") int userId,
                                             Model model, HttpSession session){
        ArrayList<Collection> collections= (ArrayList<Collection>) collectionRepository.findCollectionByUserId(userId);
        int l=collections.size();
        ArrayList<PageList> pageLists=new ArrayList<>();
        PageList pageList=new PageList();
        for(int i=0;i<l;i++){
            Documentation documentation=new Documentation();
            documentation=documentationRepository.findDocumentationById(collections.get(i).id);
            if(documentation.isTemplate==false)
                continue;
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
}
