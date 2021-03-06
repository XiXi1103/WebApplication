package com.web.repository;

import com.web.entity.Notice;
import com.web.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    List<Notice> findByUserID(Integer id);
    Notice findNoticeById(Integer id);
    Notice findNoticeByUserIDAndGroupIDAndCategory(int userID,int groupID,int category);
    Notice findNoticeByUserIDAndDocIDAndCategory(int userID,int docID,int category);
}