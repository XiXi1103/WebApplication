package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.PageList;
import com.web.entity.ReturnResult.PageListResult;
import com.web.entity.ReturnResult.Result;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.web.controller.DocumentationRecordController.isTheSameDay;

//YC:finished
@CrossOrigin
@Controller
public class CollectionController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MyTemplateRepository myTemplateRepository;
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
    public Result collection(@RequestParam int docId,
                             @RequestParam int userId,Model model, HttpSession session){
        Result result = new Result();
        System.out.println(userId + "," + docId);
        if(!CheckController.checkUserById(userId)){
            result.success = false;
            result.msg = "用户不存在";
            return result;
        }
        if(!CheckController.checkDocById(docId)){
            result.success = false;
            result.msg = "收藏文档不存在";
            return result;
        }
        Collection collection = collectionRepository.findCollectionByUserIdAndDocumentationId(userId,docId);
        if(collection==null){
            collection = new Collection();
            collection.collect_time=new Date();
            collection.documentationId=docId;
            collection.userId=userId;
            collection.status=true;
            collectionRepository.save(collection);
            result = new Result();
            result.success = true;
            result.ID =collection.id ;
            result.msg = "收藏成功!";
            return result;
        }
        else if(!collection.status){
            collection.status=true;
            collection.collect_time=new Date();
            collectionRepository.save(collection);
            result = new Result();
            result.success = true;
            result.ID =collection.id ;
            result.msg = "收藏成功!";
            return result;
        }
        else{
            collection.status=false;
            collectionRepository.save(collection);
            result = new Result();
            result.success = true;
            result.ID = collection.id ;
            result.msg = "取消收藏成功!";
            return result;
        }
    }

    @GetMapping(value = {"/getCollectionDoc"})
    @ResponseBody
    public List<PageListResult> getCollectionDoc(@RequestParam("userID") int userId,
                                                Model model, HttpSession session){
        ArrayList<Collection> collections= (ArrayList<Collection>) collectionRepository.findCollectionByUserId(userId);
        int l=collections.size();
        List<PageListResult> pageListResults = new ArrayList<>();
        for (int i=0;i<l;i++){
            Collection collection=collections.get(i);
            Date date=collection.collect_time;
            Documentation documentation = documentationRepository.findDocumentationById(collection.documentationId);
            if(documentation==null)
                return pageListResults;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,date)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.date=date;
                pageList.setDates(date);
                pageList.isCreator = userId == documentation.creatorId;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }

    @GetMapping(value = {"/getGroupDoc"})
    @ResponseBody
    public List<PageListResult> getGroupDoc(@RequestParam("groupID") int groupId,
                                          @RequestParam("userId") int userId,
                                          Model model, HttpSession session){
        ArrayList<Documentation> documentations= (ArrayList<Documentation>) documentationRepository.findDocumentationByGroupId(groupId);
        int l=documentations.size();
        List<PageListResult> pageListResults = new ArrayList<>();
        for (int i=0;i<l;i++){
            Documentation documentation=documentations.get(i);
            Date date=documentation.createTime;
            if(documentation==null)
                return pageListResults;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,date)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.date=date;
                pageList.setDates(date);
                pageList.isCreator = userId == documentation.creatorId;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }
    @GetMapping(value = {"/getMyTemplate1"})
    @ResponseBody
    public List<PageListResult> getMyTemplate(@RequestParam("userId") int userId,
                                             Model model, HttpSession session){
        if(userRepository.findById(userId) == null)
            return null;
        List<MyTemplate> myTemplates = myTemplateRepository.findByUserId(userId);
        int k=myTemplates.size();
        List<Documentation> documentations=new ArrayList<>();
        for(int i=0;i<k;i++){
            Documentation documentation=new Documentation();
            documentation=documentationRepository.findDocumentationById(myTemplates.get(i).docId);
            documentations.add(documentation);
        }
        int l=documentations.size();
        List<PageListResult> pageListResults = new ArrayList<>();
        for (int i=0;i<l;i++){
            Documentation documentation=documentations.get(i);
            Date date=documentation.createTime;
            if(documentation==null)
                return pageListResults;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = false;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,date)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = false;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.date=date;
                pageList.setDates(date);
                pageList.isCreator = false;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }

    @GetMapping(value = {"/getAllTemplate"})
    @ResponseBody
    public List<PageListResult> getAllTemplate(){
        List<Documentation> documentations = documentationRepository.findDocumentationByIsTemplate(true);
        int l=documentations.size();
        List<PageListResult> pageListResults = new ArrayList<>();
        for (int i=0;i<l;i++){
            Documentation documentation=documentations.get(i);
            Date date=documentation.createTime;
            if(documentation==null)
                return pageListResults;
            PageList pageList = new PageList();
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = false;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else if(!isTheSameDay(pageListResults.get(pageListResults.size()-1).date,date)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageListResult.setDates(date);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.isCreator = false;
                pageList.date=date;
                pageList.setDates(date);
                pageListResult.pageList=new ArrayList<>();
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);

            }
            else {
                PageListResult pageListResult=pageListResults.get(pageListResults.size()-1);
                pageList.id = documentation.id;
                pageList.title = documentation.title;
                pageList.date=date;
                pageList.setDates(date);
                pageList.isCreator = false;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
    }

    @GetMapping(value = {"/addMyTemplate"})
    @ResponseBody
    public Result addMyTemplate(@RequestParam("userID") int userId,
                                             @RequestParam int docID,
                                             Model model, HttpSession session){
        Result result = new Result();
        Documentation documentation = documentationRepository.findDocumentationById(docID);
        User user = userRepository.findUserById(userId);
        if(user == null){
            result.success = false;
            result.msg = "当前用户不存在";
            return result;
        }
        if(documentation == null || !documentation.isTemplate){
            result.success = false;
            result.msg = "该模板不存在";
            return result;
        }
        MyTemplate myTemplate = myTemplateRepository.findMyTemplateByUserIdAndDocId(userId,docID);
        if(myTemplate == null){
            myTemplate = new MyTemplate();
            myTemplate.date = new Date();
            myTemplate.docId = docID;
            myTemplate.userId = userId;
            myTemplate.status = true;
            myTemplateRepository.save(myTemplate);
            result.success = true;
            result.msg = "收藏模板成功";
        }
        else{
            result.success = false;
            result.msg = "您已收藏该模板";
        }
        return result;

    }
}
