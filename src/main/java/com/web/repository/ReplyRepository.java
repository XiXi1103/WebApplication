package com.web.repository;

import com.web.entity.Reply;
import com.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {
    Reply findReplyById(Integer id);
    List<Reply> findByDocId(Integer id);
    List<Reply> findByReplyId(Integer id);
}