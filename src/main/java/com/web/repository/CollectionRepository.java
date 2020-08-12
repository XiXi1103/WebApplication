package com.web.repository;

import com.web.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    List<Documentation> findDocumentationById(Integer id);

    Collection findCollectionByUserIdAndDocumentationId(int userId,int documentationId);

}
