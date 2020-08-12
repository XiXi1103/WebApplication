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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

            try {
                File imageFolder= new File("./doc");
                File targetFile = new File(imageFolder,String.valueOf(documentation.id));
                if(!targetFile.getParentFile().exists()){
                    targetFile.getParentFile().mkdirs();
                }
                System.out.println(targetFile.getAbsolutePath());

                documentation.path = targetFile.getPath();

                saveAsFileWriter(documentation_vue.content,documentation_vue.html,targetFile.getPath());
//            BufferedImage img = ImageUtil.change2jpg(targetFile);
//            ImageIO.write(img, "jpg", targetFile);
                /*            file.transferTo(targetFile);*/
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(realPath + file.getOriginalFilename());
//            Files.write(path, bytes);
                result.success = true;
                result.msg = "创建成功";
            } catch (Exception e) {
                result.success = false;
                result.msg = "创建失败";
                documentationRepository.delete(documentation);
                e.printStackTrace();
            }
        }
        else {
//            documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
//            documentation.content = documentation_vue.content;
//            documentation.title = documentation_vue.title;
        }

        documentationRepository.save(documentation);

        result.success = true;
        result.ID = documentation.id;
        return result;
    }

    private static void saveAsFileWriter(String content,String html,String filePath) {
        FileWriter fileWriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fileWriter = new FileWriter(filePath, true);
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

    @PostMapping(value = {"/recycle/delete"})
    @ResponseBody
    public Result recycleDelete(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        if (documentation.creatorId == documentation_vue.authorID) {
            documentationRepository.delete(documentation);
            Result result = new Result();
            result.success = true;
            result.msg = "删除文档成功!";
            return result;
        } else {
            Result result = new Result();
            result.success = false;
            result.msg = "删除文档失败，权限不足!";
            return result;
        }
    }
    
    @PostMapping(value = {"/documentation/delete"})
    @ResponseBody
    public Result delete(@RequestBody Documentation_vue documentation_vue,
                         Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        if (documentation.creatorId == documentation_vue.authorID) {
            documentation.isTrash = false;
            documentationRepository.save(documentation);
            Result result = new Result();
            result.success = true;
            result.msg = "删除文档成功!";
            return result;
        } else {
            Result result = new Result();
            result.success = false;
            result.msg = "删除文档失败，权限不足!";
            return result;
        }
    }

    @PostMapping(value = {"/documentation/update"})
    @ResponseBody
    public Documentation update(@RequestBody Documentation_vue documentation_vue,
                                Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(documentation_vue.authorID, documentation.groupId);
            if (groupMember.permission >= 4) {
                return documentation;
            } else {
                return null;
            }
        }
        else{
            if(documentation_vue.authorID == documentation.creatorId){
                return documentation;
            }
            else if(documentation.otherPermission >= 4){
                return documentation;
            }
            else{
                return null;
            }
        }
    }
//
    @PostMapping(value = {"/documentation/show"})
    @ResponseBody
    public Documentation show(@RequestBody Documentation_vue documentation_vue,
                              Model model, HttpSession session) {
        Documentation documentation = documentationRepository.findDocumentationById(documentation_vue.docID);
        if (documentation.groupId != 0) {
            GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(documentation_vue.authorID, documentation.groupId);
            if (groupMember.permission >= 1) {
                return documentation;
            } else {
                return null;
            }
        }
        else{
            if(documentation_vue.authorID == documentation.creatorId){
                return documentation;
            }
            else if(documentation.otherPermission >= 1){
                return documentation;
            }
            else{
                return null;
            }
        }
    }

}