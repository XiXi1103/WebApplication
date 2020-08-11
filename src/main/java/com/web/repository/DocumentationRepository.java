package com.web.repository;

import com.web.entity.Documentation;
import com.web.entity.User;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentationRepository extends JpaRepository<Documentation,Integer> {
    Documentation findDocumentationById(Integer id);
    List<Documentation> findByTitle(String title);
    List<Documentation> findByCreatorId(Integer id);
    List<Documentation> findByGroupId(Integer id);
}
