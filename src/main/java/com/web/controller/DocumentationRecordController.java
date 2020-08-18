package com.web.controller;

import com.web.entity.Documentation;
import com.web.entity.DocumentationRecord;
import com.web.entity.GroupMember;
import com.web.entity.ReturnResult.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

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
//    @GetMapping(value = {"/getRecentDoc"})
//    @ResponseBody
//    public List<PageList> getRecentDoc(@RequestParam int userId, Model model, HttpSession session){
//        List<DocumentationRecord> documentationRecords=documentationRecordRepository.findDocumentationRecordByUserId(userId);
//        //Collections.sort(documentationRecords);
//        List<PageList> pageLists=new ArrayList<>();
//        System.out.println("1");
//        int l = documentationRecords.size();
//        for (DocumentationRecord documentationRecord : documentationRecords) {
//            PageList pageList = new PageList();
//            Documentation documentation = documentationRepository.findDocumentationById(documentationRecord.documentationId);
//            if (documentation == null)
//                continue;
//            pageList.id = documentationRecord.documentationId;
//            pageList.title = documentation.title;
//            pageList.isCreator = userId == documentation.creatorId;
//            pageLists.add(pageList);
//        }
//        return pageLists;
//    }
public  boolean isTheSameDay(Date d1,Date d2) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(d1);
    c2.setTime(d2);
    return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
            && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
            && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
}
    @GetMapping(value = {"/getRecentDoc"})
    @ResponseBody
    public List<PageListResult> getRecentDoc(@RequestParam int userId, Model model, HttpSession session){
        List<DocumentationRecord> documentationRecords=documentationRecordRepository.findDocumentationRecordByUserId(userId);
        //Collections.sort(documentationRecords);
        List<PageListResult> pageListResults=new ArrayList<>();
        int l = documentationRecords.size();
        if(l==0)
            return pageListResults;
        int k=0;
        for (int i=0;i<l;i++){
            DocumentationRecord documentationRecord=documentationRecords.get(i);
            Date date=documentationRecord.time;
            PageList pageList = new PageList();
            Documentation documentation = documentationRepository.findDocumentationById(documentationRecord.documentationId);

            if (documentation == null)
                continue;
            if(i==0){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageList.id = documentationRecord.documentationId;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);
            }
            else if(!isTheSameDay(pageListResults.get(k).date,documentationRecord.time)){
                PageListResult pageListResult=new PageListResult();
                pageListResult.date=date;
                pageList.id = documentationRecord.documentationId;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageListResult.pageList.add(pageList);
                pageListResults.add(pageListResult);
            }
            else {
                PageListResult pageListResult=pageListResults.get(k);
                pageList.id = documentationRecord.documentationId;
                pageList.title = documentation.title;
                pageList.isCreator = userId == documentation.creatorId;
                pageListResult.pageList.add(pageList);
            }
        }
        return pageListResults;
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
