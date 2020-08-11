package com.web.repository;

import com.web.entity.Likes;
import com.web.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Integer> {
    Likes findLikesById(Integer id);
    Likes findByDocIdAndReplyId(Integer docId,Integer replyId);
    List<Likes> findByDocId(Integer id);
    List<Likes> findByReplyId(Integer id);
}
