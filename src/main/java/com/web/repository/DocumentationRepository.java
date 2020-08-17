package com.web.repository;

import com.web.entity.Documentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentationRepository extends JpaRepository<Documentation,Integer> {
    Documentation findDocumentationById(Integer id);
    List<Documentation> findDocumentationByTitle(String title);
    @Query(value="select * from documentation where isTrash == false ", nativeQuery=true)
    List<Documentation> findDocumentationByCreatorId(Integer id);
    List<Documentation> findDocumentationByTemplate(Boolean template);
    List<Documentation> findDocumentationByGroupId(Integer id);
}
