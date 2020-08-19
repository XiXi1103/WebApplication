package com.web.repository;

import com.web.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    List<Collection> findDocumentationById(Integer id);
    @Query(value="select * from collection  where user_id=?1 and status=1 order by collect_time desc", nativeQuery=true)
    List<Collection> findCollectionByUserId(Integer id);
    Collection findCollectionByUserIdAndDocumentationId(int userId,int documentationId);

}
