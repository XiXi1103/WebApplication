package com.web.repository;

import com.web.entity.Documentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentationRepository extends JpaRepository<Documentation,Integer> {
    Documentation findDocumentationById(Integer id);
    @Query(value="select * from documentation  where other_permisssion > 0 order by create_time desc", nativeQuery=true)
    List<Documentation> findDocumentationByTitleContaining(String title);
//    @Query(value="select * from article where category_name=?1 order by pinned DESC,id DESC ", nativeQuery=true)
    @Query(value="select * from documentation  where is_trash = 0 and creator_id=?1 order by create_time desc", nativeQuery=true)
    List<Documentation> findDocumentationByCreatorId(Integer id);
    @Query(value="select * from documentation  where is_trash = 1 and creator_id=?1 order by create_time desc", nativeQuery=true)
    List<Documentation> findDocumentationByCreatorIdAndTrash(Integer id);
    List<Documentation> findDocumentationByIsTemplate(Boolean template);
    @Query(value="select * from documentation  where is_trash = 0 and group_id=?1 order by create_time desc", nativeQuery=true)
    List<Documentation> findDocumentationByGroupId(Integer id);
}
