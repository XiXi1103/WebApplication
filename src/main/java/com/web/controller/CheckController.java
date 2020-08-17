package com.web.controller;

import com.web.entity.*;
import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PostConstruct;

@CrossOrigin
@Controller
@Component
public class CheckController {
    @Autowired
    UserRepository userRepository1;
    @Autowired
    CollaboratorRepository collaboratorRepository1;
    @Autowired
    GroupRepository groupRepository1;
    @Autowired
    GroupMemberRepository groupMemberRepository1;
    @Autowired
    ReplyRepository replyRepository1;
    @Autowired
    LikesRepository likesRepository1;
    @Autowired
    DocumentationRepository documentationRepository1;
    @Autowired
    NoticeRepository noticeRepository1;
    @Autowired
    CollectionRepository collectionRepository1;
    @Autowired
    DocumentationRecordRepository documentationRecordRepository1;
    @Autowired
    DocumentModificationRecordRepository documentModificationRecordRepository1;

    public static UserRepository userRepository;
    public static DocumentationRepository documentationRepository;
    public static ReplyRepository replyRepository;
    public static GroupRepository groupRepository;
    public static LikesRepository likesRepository;
    public static CollaboratorRepository collaboratorRepository;
    public static CollectionRepository collectionRepository;
    public static DocumentationRecordRepository documentationRecordRepository;
    public static DocumentModificationRecordRepository documentModificationRecordRepository;
    public static GroupMemberRepository groupMemberRepository;
    public static NoticeRepository noticeRepository;

    @PostConstruct
    public void init() {
        collaboratorRepository = collaboratorRepository1;
        collectionRepository = collectionRepository1;
        documentationRepository = documentationRepository1;
        documentationRecordRepository = documentationRecordRepository1;
        documentModificationRecordRepository = documentModificationRecordRepository1;
        groupMemberRepository = groupMemberRepository1;
        groupRepository = groupRepository1;
        likesRepository = likesRepository1;
        noticeRepository = noticeRepository1;
        replyRepository = replyRepository1;
        userRepository = userRepository1;
    }
    public static boolean checkUserById(int userId){
        User user = userRepository.findUserById(userId);
        return user != null;
    }

    public static boolean checkUserByUsername(String username){
        User user = userRepository.findUserByUsername(username);
        return user != null;
    }

    public static boolean checkDocById(int docId){
        Documentation documentation = documentationRepository.findDocumentationById(docId);
        return documentation != null;
    }

    public static boolean checkGroupById(int groupId){
        Group group = groupRepository.findGroupById(groupId);
        return group != null;
    }

    public static boolean checkNoticeById(int noticeId){
        Notice notice = noticeRepository.findNoticeById(noticeId);
        return notice != null;
    }


}
