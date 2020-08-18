package com.web.repository;


import com.web.entity.Documentation;
import com.web.entity.MyTemplate;
import com.web.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyTemplateRepository extends JpaRepository<MyTemplate,Integer> {
    MyTemplate findMyTemplateByUserIdAndDocId(Integer userId,Integer docId);

    List<MyTemplate> findByUserId(Integer userId);
}