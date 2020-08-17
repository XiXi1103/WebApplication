package com.web.controller;

import com.web.entity.Documentation;
import com.web.entity.DocumentationRecord;
import com.web.entity.GroupMember;
import com.web.entity.ReturnResult.GroupList;
import com.web.entity.ReturnResult.PageList;
import com.web.entity.ReturnResult.RemoveRecentBrowsingResult;
import com.web.entity.ReturnResult.Result;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

@CrossOrigin
@Controller
public class DocumentationRecordController {
    @Autowired
    DocumentationRecordRepository documentationRecordRepository;
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
    public List<PageList> getRecentDoc(@RequestParam("userID") int userId, Model model, HttpSession session){
        List<DocumentationRecord> documentationRecords=documentationRecordRepository.findDocumentationRecordByUserId(userId);
        //Collections.sort(documentationRecords);
        List<PageList> pageLists=new ArrayList<>();
        int l = documentationRecords.size();
        for(int i = 0 ; i < l; i++){
            PageList pageList=new PageList();
            Documentation documentation=documentationRepository.findDocumentationById(documentationRecords.get(i).documentationId);
            if(documentation==null)
                continue;
            pageList.id = documentationRecords.get(i).documentationId;
            pageList.title = documentation.title;
            pageList.isCreator= userId == documentation.creatorId;
            pageLists.add(pageList);
        }
        return pageLists;
    }
    @GetMapping(value = {"/removeRecentBrowsing"})
    @ResponseBody
    public Result removeRecentBrowsing(@RequestParam("userID") int userId, @RequestParam("docID") int docId, Model model, HttpSession session){
        DocumentationRecord documentationRecord=documentationRecordRepository.findDocumentationRecordByUserIdAndDocumentationId(userId,docId);
        documentationRecordRepository.delete(documentationRecord);
        Result result=new Result();
        result.success=true;
        result.msg="删除成功";
        return result;
    }
    public static void addRecord(int userId, int docId, Date time,DocumentationRecordRepository documentationRecordRepository1){
        DocumentationRecord documentationRecord=documentationRecordRepository1.findDocumentationRecordByUserIdAndDocumentationId(userId,docId);
        if(documentationRecord!=null){
            documentationRecord.time=time;
            documentationRecordRepository1.save(documentationRecord);
        }
        else{
            DocumentationRecord documentationRecord1=new DocumentationRecord();
            documentationRecord1.userId=userId;
            documentationRecord1.documentationId=docId;
            documentationRecord1.time=time;
            documentationRecordRepository1.save(documentationRecord1);
        }
    }
}
