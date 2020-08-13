package com.web.controller;

import com.web.entity.*;
import com.web.repository.CollaboratorRepository;
import com.web.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class CollaboratorController {
    @Autowired
    CollaboratorRepository collaboratorRepository;
    @GetMapping(value = {"/addWriter"})
    @ResponseBody
    public Result addWriter(@RequestParam("userID1") int userId1,
                                        @RequestParam("userID2") int userId2,
                                        @RequestParam("docID") int docId,
                                        @RequestParam("permission") int permission,
                                       Model model, HttpSession session){
        Result result = new  Result();
        result.success=false;
        result.msg="权限不足";
        if(collaboratorRepository.findCollaboratorByUserIdAndAndDocumentationId(userId1,docId).permission==4)
            return result;
        Collaborator collaborator=new Collaborator();
        collaborator.documentationId=docId;
        collaborator.permission=permission;
        collaborator.userId=userId2;
        collaboratorRepository.save(collaborator);
        result.success=true;
        result.msg="邀请成功";
        return result;
    }
}
