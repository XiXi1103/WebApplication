package com.web.controller;

import com.web.entity.*;
import com.web.repository.DocumentationRepository;
import com.web.repository.GroupMemberRepository;
import com.web.repository.GroupRepository;
import com.web.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

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

    @PostMapping(value = {"/newDoc"})
    @ResponseBody
    public Result create(@RequestBody Documentation_vue documentation_vue,
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
        if (documentation_vue.content == null) {
            result.success = false;
            result.ID = 0;
            result.msg = "请填写内容";
            return result;
        }
        if (creatorId == 0) {
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
            documentation.creatorId = documentation_vue.authorID;

            documentationRepository.save(documentation);

        }
        else {
            documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        }

        saveDoc(documentation_vue, result, documentation);

        documentationRepository.save(documentation);
        if(documentation_vue.docID != 0){
            result.msg = "替换成功";
        }
        result.ID = documentation.id;
        return result;
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

    @PostMapping(value = {"/delDocCompletely"})
    @ResponseBody
    public Result delDocCompletely(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        Result result = new Result();
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
    
    @PostMapping(value = {"/delDoc"})
    @ResponseBody
    public Result delDoc(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        Result result = new Result();
        if (documentation.creatorId == documentation_vue.userID) {
            documentation.isTrash = true;
            documentationRepository.save(documentation);
            result.success = true;
            result.msg = "删除文档成功!";
        }
        else if(documentation.groupId != 0){
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(documentation_vue.userID,documentation.groupId);
            if(groupMember.permission >= 4){
                documentation.isTrash = true;
                documentationRepository.save(documentation);
                result.success = true;
                result.msg = "删除团队文档成功!";
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

    @GetMapping(value = {"/editDoc"})
    @ResponseBody
    public DocResult update(@RequestParam int userID,
                                @RequestParam int docID,
                                Model model, HttpSession session) {
        DocResult docResult = new DocResult();
        Documentation documentation = documentationRepository.findDocumentationById(docID);
        if(documentation == null){
            docResult.success = false;
            docResult.msg = "文档不存在";
            return docResult;
        }
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(userID, documentation.groupId);
            if (groupMember.permission >= 4) {
                docResult = getDocResult(docResult, documentation);
                docResult.success = true;
                docResult.msg = "修改成功";
            } else {
                docResult.success = false;
                docResult.msg = "权限不足，无法修改";
            }
        }
        else{
            if(userID == documentation.creatorId || documentation.otherPermission >= 4){
                docResult = getDocResult(docResult, documentation);
                docResult.success = true;
                docResult.msg = "修改成功";
            }
            else{
                docResult.success = false;
                docResult.msg = "权限不足，无法修改";
            }
        }
        return docResult;
    }

    @PostMapping(value = {"/recoverDoc"})
    @ResponseBody
    public Result recoverDoc(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        Result result = new Result();
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
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(documentation_vue.userID,documentation.groupId);
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
    public DocResult showDoc(@RequestParam int userID,
                             @RequestParam int docID,
                              Model model, HttpSession session) {
        DocResult docResult = new DocResult();
        Documentation documentation = documentationRepository.findDocumentationById(docID);
        if(documentation == null){
            docResult.success = false;
            docResult.msg = "文档不存在";
            return docResult;
        }
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(userID, documentation.groupId);
            if (groupMember.permission >= 1) {
                docResult = getDocResult(docResult,documentation);
                docResult.success = true;
                docResult.msg = "显示成功";
            } else {
                docResult.success = false;
                docResult.msg = "权限不足，无法查看";
            }
        }
        else{
            if(userID == documentation.creatorId || documentation.otherPermission >= 1){
                docResult = getDocResult(docResult,documentation);
                docResult.success = true;
                docResult.msg = "显示成功";
            }
            else{
                docResult.success = false;
                docResult.msg = "权限不足，无法查看";
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


    private DocResult getDocResult(DocResult docResult, Documentation documentation) {
        Map<String,String> docMap = getDoc(documentation);
        docResult.content = docMap.get("content");
        docResult.html = docMap.get("html");
        return docResult;
    }
//

    public static String readFileByChars(String fileName) {
        File file = new File(fileName);
        String res = new String();
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
                    res += tempChars;
                } else {
                    for (int i = 0; i < charRead; i++) {
                        if (tempChars[i] == '\r') {
                            continue;
                        } else {
//                            System.out.print(tempchars[i]);
                            res += tempChars[i];
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
                } catch (IOException e1) {
                }
            }
        }
        return res;
    }
}