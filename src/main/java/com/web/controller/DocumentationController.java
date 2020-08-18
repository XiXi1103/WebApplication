package com.web.controller;

import com.web.entity.*;
import com.web.entity.Collection;
import com.web.entity.ReturnResult.*;
import com.web.entity.vue.Documentation_vue;
import com.web.repository.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

import static com.web.controller.DocumentationRecordController.isTheSameDay;


//YC:finished
@CrossOrigin
@Controller
public class DocumentationController {
    @Autowired
    DocumentationRepository documentationRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @Autowired
    CollaboratorRepository collaboratorRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    DocumentModificationRecordRepository documentModificationRecordRepository;
    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    DocumentationRecordRepository documentationRecordRepository;
    @PostMapping(value = {"/newDoc"})
    @ResponseBody
    public Result newDoc(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        int creatorId = documentation_vue.authorID;
        String title = documentation_vue.title;
        Result result = new Result();
        if (title == null) {
            result.success = false;
            result.ID = 0;
            result.msg = "请填写标题";
            return result;
        }
        if (documentation_vue.html == null ) {
            result.success = false;
            result.ID = 0;
            result.msg = "请填写内容";
            return result;
        }
        if (creatorId == 0 && documentation_vue.userID == 0) {
            result.success = false;
            result.ID = 0;
            result.msg = "请登录";
            return result;
        }
        Documentation documentation = new Documentation();
        if(documentation_vue.docID == 0){
            documentation = new Documentation();
            documentation.title = documentation_vue.title;
            documentation.createTime = new Date();
            documentation.summary = documentation_vue.summary;
            documentation.creatorId = documentation_vue.authorID;
            documentation.otherPermission=documentation_vue.permission;
            documentation.groupId = documentation_vue.groupId;
            documentation.lastTime=documentation.createTime;
            documentation.isEdit=false;
            documentation.editorId=0;
            documentationRepository.save(documentation);
            DocumentationRecordController.addRecord(documentation_vue.authorID,documentation.id,documentation.lastTime,documentationRecordRepository);

        }
        else {
            documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
            documentation.title = documentation_vue.title;
            documentation.otherPermission=documentation_vue.otherPermission;
            documentation.lastTime=new Date();
            documentation.isEdit=false;
            documentation.editorNum++;
            DocumentModificationRecord documentModificationRecord =new DocumentModificationRecord();
            documentModificationRecord.docId=documentation_vue.docID;
            documentModificationRecord.userId=documentation.editorId;
            documentModificationRecord.time=documentation.lastTime;
            documentation.editorId=0;
            DocumentationRecordController.addRecord(documentation_vue.userID,documentation.id,documentation.lastTime,documentationRecordRepository);

            //团队文档被修改发送通知
            if(documentation.groupId != 0){
                int category = 1;
                generateNoticeAboutGroupDoc(documentation_vue, documentation, category);
            }
        }
        saveDoc(documentation_vue, result, documentation);
        documentationRepository.save(documentation);
        if(documentation_vue.docID != 0){
            result.msg = "修改成功";
        }
        result.ID = documentation.id;
        return result;
    }

    private void generateNoticeAboutGroupDoc(@RequestBody Documentation_vue documentation_vue, Documentation documentation, int category) {
        List<GroupMember> groupMembers= groupMemberRepository.findGroupMemberByGroupId(documentation.groupId);
        for (GroupMember groupMember : groupMembers) {
            if(documentation_vue.authorID==groupMember.userId)
                continue;
            Notice notice = new NoticeController().addNoticeAboutGroupDoc(groupMember.userId, documentation_vue.authorID,
                    category, documentation.groupId, documentation.id,
                    userRepository, documentationRepository, groupRepository);
            noticeRepository.save(notice);
        }
    }
    private void saveDoc(@RequestBody Documentation_vue documentation_vue, Result result, Documentation documentation) {
        try {
            File docFolder= new File("../doc");
            File targetFile = new File(docFolder,String.valueOf(documentation.id));
            if(!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }
            File targetFile_1 = new File(targetFile,"content.txt");//保存内容
            File targetFile_2 = new File(targetFile,"html.txt");//保存html

            if(!targetFile_1.getParentFile().exists()){
                targetFile_1.getParentFile().mkdirs();
            }

//            System.out.println(targetFile_1.getAbsolutePath());
//            System.out.println(targetFile_2.getAbsolutePath());

            documentation.path = targetFile.getPath();

            saveAsFileWriter(documentation_vue.content,targetFile_1.getPath());
            saveAsFileWriter(documentation_vue.html,targetFile_2.getPath());

            result.success = true;
            result.msg = "创建成功";
        } catch (Exception e) {
            result.success = false;
            result.msg = "创建失败";
            documentationRepository.delete(documentation);
            e.printStackTrace();
        }
    }

    private static void saveAsFileWriter(String content,String filePath) {
        FileWriter fileWriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fileWriter = new FileWriter(filePath, false);
            fileWriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //图片上传
    @RequestMapping(value="/imgAdd")
    @ResponseBody
    public Result demo(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        Result result = new Result();
        //保存
        try {
            File imageFolder= new File("../uploadImg");
            File targetFile = new File(imageFolder,file.getOriginalFilename());
            if(!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();

            }
//            System.out.println(targetFile.getAbsolutePath());
            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
//            BufferedImage img = ImageUtil.change2jpg(targetFile);
//            ImageIO.write(img, "jpg", targetFile);
            /*            file.transferTo(targetFile);*/
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(realPath + file.getOriginalFilename());
//            Files.write(path, bytes);
                result.success = true;
                result.msg = targetFile.getAbsolutePath();
        } catch (Exception e) {
                result.success = false;
                result.msg = "图片上传失败";
            e.printStackTrace();
        }
        return result;
    }
//
//    public static String limitLength(String name,int length){
//        return name.substring(0,Math.min(name.length(),length));
//    }


    @GetMapping(value = {"/delDocCompletely"})
    @ResponseBody
    public Result delDocCompletely(@RequestParam int userId,@RequestParam int docId,
    Model model, HttpSession session) {
        Documentation_vue documentation_vue=new Documentation_vue();
        documentation_vue.docID=docId;
        documentation_vue.userID=userId;
        Result result = new Result();
        if(!CheckController.checkUserById(documentation_vue.userID)){
            result.success = false;
            result.msg = "用户不存在";
            return result;
        }
        if(!CheckController.checkDocById(documentation_vue.docID)){
            result.success = false;
            result.msg = "该文档不存在";
            return result;
        }
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        if(!documentation.isTrash){
            result.success = false;
            result.msg = "该文档未在回收站内，无法彻底删除";
        }
        else if (documentation.creatorId == documentation_vue.userID) {
            documentationRepository.delete(documentation);
            result.success = true;
            result.msg = "删除文档成功!";
        } else {
            result.success = false;
            result.msg = "删除文档失败，权限不足!";
        }
        return result;
    }
    @GetMapping(value = {"/modifyRecord"})
    @ResponseBody
    public ArrayList<MemberList> modifyRecord(@RequestParam("docId") int docId,
                                           Model model, HttpSession session){
        ArrayList<MemberList> memberLists=new ArrayList<>();
        Documentation documentation=documentationRepository.findDocumentationById(docId);
        List<DocumentModificationRecord> documentModificationRecords=
                documentModificationRecordRepository.findDocumentModificationRecordsByDocId(docId);
        for (DocumentModificationRecord documentModificationRecord : documentModificationRecords) {
            MemberList memberList=new MemberList();
            memberList.id = documentModificationRecord.userId;
            memberList.name = userRepository.findUserById(documentModificationRecord.userId).username;
            memberList.time=documentModificationRecord.time.toString();
            memberList.msg=memberList.name+"在"+memberList.time+"修改了"+documentation.title;
            memberLists.add(memberList);
        }
        return memberLists;
    }
    @GetMapping(value = {"/delDoc"})
    @ResponseBody
    public Result delDoc(@RequestParam int userId,@RequestParam int docId,
                         Model model, HttpSession session) {
        Documentation_vue documentation_vue=new Documentation_vue();
        documentation_vue.docID=docId;
        documentation_vue.userID=userId;
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        Result result = new Result();
        if(!CheckController.checkUserById(documentation_vue.userID)){
            result.success = false;
            result.msg = "用户不存在";
            return result;
        }
        if(!CheckController.checkDocById(documentation_vue.docID)){
            result.success = false;
            result.msg = "该文档不存在";
            return result;
        }
        if (documentation.creatorId == documentation_vue.userID) {
            documentation.isTrash = true;
            documentationRepository.save(documentation);
            result.success = true;
            result.msg = "删除文档成功!";
        }
        else if(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userId,docId) != null && collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userId,docId).permission == 5){
            documentation.isTrash = true;
            documentationRepository.save(documentation);
            result.success = true;
            result.msg = "删除协作文档成功!";
            int category = 6;
            List<Collaborator> collaboratorList = collaboratorRepository.findCollaboratorByDocumentationId(documentation_vue.docID);
            for(Collaborator collaborator : collaboratorList) {
                new NoticeController().addNoticeAboutDoc(collaborator.userId, documentation_vue.userID, category,documentation.id,0,userRepository,documentationRepository,replyRepository);
            }
            if(documentation.groupId != 0){
                generateNoticeAboutGroupDoc(documentation_vue, documentation, 2);
            }
        }
        else if(documentation.groupId != 0){
            GroupMember groupMember = groupMemberRepository.findGroupMemberByUserIdAndGroupId(documentation_vue.userID,documentation.groupId);
            if(groupMember.permission >= 4){
                documentation.isTrash = true;
                documentationRepository.save(documentation);
                result.success = true;
                result.msg = "删除团队文档成功!";
                int category = 2;
                generateNoticeAboutGroupDoc(documentation_vue, documentation, category);
            }
            else{
                result.success = false;
                result.msg = "删除文档失败，权限不足!";
            }
        }
        else {
            result.success = false;
            result.msg = "删除文档失败，权限不足!";
        }
        return result;
    }

    //@倪某，协作者
    @GetMapping(value = {"/editDoc"})
    @ResponseBody
    public EditDocResult update(@RequestParam int userID,
                                @RequestParam int docID,
                                Model model, HttpSession session) {
        EditDocResult docResult = new EditDocResult();
        if(!CheckController.checkUserById(userID)){
            docResult.success = false;
            docResult.msg = "当前用户不存在";
            return docResult;
        }
        Documentation documentation = documentationRepository.findDocumentationById(docID);
        if(documentation == null){
            docResult.success = false;
            docResult.msg = "文档不存在";
            return docResult;
        }
        docResult.title = documentation.title;
        docResult.currentPermission = documentation.otherPermission;
        long time=new Date().getTime()-documentation.lastTime.getTime();
        if(documentation.isEdit&&time<=1800000000){
            docResult.success = false;
            docResult.msg = "文档正在被"+userRepository.findUserById(documentation.editorId).username+"编辑";
            return docResult;
        }
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findGroupMemberByUserIdAndGroupId(userID, documentation.groupId);
            if (groupMember.permission >= 4) {
                getDocResult(docResult, documentation);
                docResult.success = true;
                docResult.msg = "返回成功";
                docResult.userPermission = groupMember.permission;
                documentation.isEdit=true;
                documentation.editorId=userID;
                DocumentationRecordController.addRecord(userID,docID,new Date(),documentationRecordRepository);
                documentation.lastTime=new Date();
                documentationRepository.save(documentation);
            } else {
                docResult.success = false;
                docResult.msg = "权限不足，无法修改";
            }
        }
        else{
            if(userID == documentation.creatorId){
                docResult = getDocResult(docResult, documentation);
                docResult.success = true;
                docResult.msg = "返回成功";
                docResult.userPermission = 5;
                documentation.isEdit=true;
                documentation.editorId=userID;
                documentation.lastTime=new Date();
                documentationRepository.save(documentation);
                DocumentationRecordController.addRecord(userID,docID,new Date(),documentationRecordRepository);
                int category=5;
                Notice notice;
                ArrayList<Collaborator> collaborators=collaboratorRepository.findCollaboratorByDocumentationId(docID);
                int l=collaborators.size();
                for(int i=0;i<l;i++){
                    notice = new NoticeController().addNoticeAboutDoc(
                            collaborators.get(i).userId, userID,category,docID,0,
                            userRepository,documentationRepository,replyRepository);
                    noticeRepository.save(notice);
                }

            }
            else if(documentation.otherPermission >= 4){
                docResult = getDocResult(docResult, documentation);
                docResult.success = true;
                docResult.msg = "返回成功";
                docResult.currentPermission = documentation.otherPermission;
                documentation.isEdit=true;
                documentation.editorId=userID;
                documentation.lastTime=new Date();
                documentationRepository.save(documentation);
                DocumentationRecordController.addRecord(userID,docID,new Date(),documentationRecordRepository);
                int category=5;
                Notice notice;
                ArrayList<Collaborator> collaborators=collaboratorRepository.findCollaboratorByDocumentationId(docID);
                int l=collaborators.size();
                notice = new NoticeController().addNoticeAboutDoc(
                        documentation.creatorId, userID,category,docID,0,
                        userRepository,documentationRepository,replyRepository);
                noticeRepository.save(notice);
                for (Collaborator collaborator : collaborators) {
                    if (collaborator.userId == userID)
                        continue;
                    notice = new NoticeController().addNoticeAboutDoc(
                            collaborator.userId, userID, category, docID, 0,
                            userRepository, documentationRepository, replyRepository);
                    noticeRepository.save(notice);
                }
            }
            else{
                docResult.success = false;
                docResult.msg = "权限不足，无法修改";
            }
        }
        return docResult;
    }

    @GetMapping(value = {"/recoverDoc"})
    @ResponseBody
    public Result recoverDoc(@RequestParam int userId,@RequestParam int docId,
                         Model model, HttpSession session) {
        Documentation_vue documentation_vue=new Documentation_vue();
        documentation_vue.userID=userId;
        documentation_vue.docID=docId;
        Result result = new Result();
        if(!CheckController.checkUserById(documentation_vue.userID)){
            result.success = false;
            result.msg = "用户不存在";
            return result;
        }
        if(!CheckController.checkDocById(documentation_vue.docID)){
            result.success = false;
            result.msg = "该文档不存在";
            return result;
        }
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        if (documentation.creatorId == documentation_vue.userID) {
            if(!documentation.isTrash){
                result.success = false;
                result.msg = "恢复文档失败，该文档不在回收站中！";
            }
            else {
                documentation.isTrash = false;
                documentationRepository.save(documentation);
                result.success = true;
                result.msg = "恢复文档成功!";
            }
        }
        else if(documentation.groupId != 0){
            GroupMember groupMember = groupMemberRepository.findGroupMemberByUserIdAndGroupId(documentation_vue.userID,documentation.groupId);
            if(groupMember.permission >= 4){
                documentation.isTrash = true;
                documentationRepository.save(documentation);
                result.success = true;
                result.msg = "恢复团队文档成功!";
            }
            else{
                result.success = false;
                result.msg = "恢复文档失败，权限不足!";
            }
        }
        else {
            result.success = false;
            result.msg = "恢复文档失败，权限不足!";
        }
        return result;
    }



    @GetMapping(value = {"/viewDoc"})
    @ResponseBody
    public DocResult showDoc(@RequestParam(value = "userId") int userID,
                             @RequestParam(value = "docId") int docID,
                             Model model, HttpSession session) {
        DocResult docResult = new DocResult();
        Documentation documentation = documentationRepository.findDocumentationById(docID);
        Collection collection =  collectionRepository.findCollectionByUserIdAndDocumentationId(userID,docID);
        if(documentation == null){
            docResult.success = false;
            docResult.msg = "文档不存在";
            docResult.permission=0;
            return docResult;
        }
        if(!CheckController.checkUserById(userID)){
            docResult.success = false;
            docResult.msg = "用户不存在";
            return docResult;
        }
        if(documentation.isTemplate){
            getDocResult(docResult, documentation);
            docResult.isCollect = false;
            docResult.isTemplate = true;
            docResult.success = true;
            docResult.permission = documentation.otherPermission;
            docResult.msg = "";
            return  docResult;
        }
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findGroupMemberByUserIdAndGroupId(userID, documentation.groupId);
            if (groupMember.permission >= 1) {
                getDocResult(docResult, documentation);
                docResult.success = true;
                if(collection == null){
                    docResult.isCollect = false;
                }
                else{
                    docResult.isCollect = collection.status;
                }
                docResult.msg = "显示成功";
                docResult.permission=groupMember.permission;
                docResult.isTemplate = documentation.isTemplate;
                addRecentUse(userID,docID);
                DocumentationRecordController.addRecord(userID,docID,documentation.lastTime,documentationRecordRepository);
            } else {
                docResult.success = false;
                docResult.msg = "权限不足，无法查看";
                docResult.permission=0;
            }
        }
        else if(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userID,docID)!=null){
            docResult.success = true;
            docResult.msg = "显示成功";
            docResult.permission = collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userID,docID).permission;
            if(collection == null){
                docResult.isCollect = false;
            }
            else{
                docResult.isCollect = collection.status;
            }
            DocumentationRecordController.addRecord(userID,docID,documentation.lastTime,documentationRecordRepository);
            docResult.permission=collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userID,docID).permission;
        }
        else{
            if(userID == documentation.creatorId){
                if(collection == null){
                    docResult.isCollect = false;
                }
                else{
                    docResult.isCollect = collection.status;
                }
                getDocResult(docResult, documentation);
                docResult.isTemplate = documentation.isTemplate;
                docResult.success = true;
                docResult.msg = "显示成功";
                docResult.permission = 5;
                addRecentUse(userID,docID);
                DocumentationRecordController.addRecord(userID,docID,documentation.lastTime,documentationRecordRepository);
            }
            else if(documentation.otherPermission >= 1){
                if(collection == null){
                    docResult.isCollect = false;
                }
                else{
                    docResult.isCollect = collection.status;
                }
                getDocResult(docResult, documentation);
                docResult.isTemplate = documentation.isTemplate;
                docResult.success = true;
                docResult.msg = "显示成功";
                docResult.permission=documentation.otherPermission;
                addRecentUse(userID,docID);
                DocumentationRecordController.addRecord(userID,docID,documentation.lastTime,documentationRecordRepository);
            }
            else{
                docResult.success = false;
                docResult.msg = "权限不足，无法查看";
                docResult.permission=0;
            }
        }
        return docResult;
    }



    private Map<String,String> getDoc(Documentation documentation){
        String content = readFileByChars(documentation.path + "/content.txt");
        String html = readFileByChars(documentation.path + "/html.txt");
        Map<String,String> res = new HashMap<>();
        res.put("content",content);
        res.put("html",html);
        return res;
    }

    public void addRecentUse(int userID,int docID){
        User user=userRepository.findUserById(userID);
        if(user.recently_usednum==5){
            user.recently_used1=user.recently_used2;
            user.recently_used2=user.recently_used3;
            user.recently_used3=user.recently_used4;
            user.recently_used4=user.recently_used5;
            user.recently_used5=docID;
        }
        else if(user.recently_usednum==4)
            user.recently_used5=docID;
        else if(user.recently_usednum==3)
            user.recently_used4=docID;
        else if(user.recently_usednum==2)
            user.recently_used3=docID;
        else if(user.recently_usednum==1)
            user.recently_used2=docID;
        else if(user.recently_usednum==0)
            user.recently_used1=docID;
    }

    private DocResult getDocResult(DocResult docResult, Documentation documentation) {
        Map<String,String> docMap = getDoc(documentation);
        docResult.content = docMap.get("content");
        docResult.html = docMap.get("html");
        return docResult;
    }

    private EditDocResult getDocResult(EditDocResult docResult, Documentation documentation) {
        Map<String,String> docMap = getDoc(documentation);
        docResult.content = docMap.get("content");
        docResult.html = docMap.get("html");
        return docResult;
    }
//

    public static String readFileByChars(String fileName) {
        File file = new File(fileName);
        StringBuilder res = new StringBuilder();
        Reader reader = null;
//        try {
//            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
//            // 一次读一个字符
//            reader = new InputStreamReader(new FileInputStream(file));
//            int tempchar;
//            while ((tempchar = reader.read()) != -1) {
//                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
//                // 但如果这两个字符分开显示时，会换两次行。
//                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
//                if (((char) tempchar) != '\r') {
//                    System.out.print((char) tempchar);
//                }
//            }
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            //System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempChars = new char[30];
            int charRead = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charRead = reader.read(tempChars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charRead == tempChars.length)
                        && (tempChars[tempChars.length - 1] != '\r')) {
//                    System.out.print(tempchars);
                    res.append(tempChars);
                } else {
                    for (int i = 0; i < charRead; i++) {
                        if (tempChars[i] == '\r') {
                        } else {
//                            System.out.print(tempchars[i]);
                            res.append(tempChars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return res.toString();
    }

    @GetMapping(value = {"/getMyDoc"})
    @ResponseBody
    public List<PageListResult> getMyDoc(@RequestParam(value = "userID") int userId,
                                         Model model, HttpSession session){

        List<Documentation> documentations= documentationRepository.findDocumentationByCreatorId(userId);
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
}