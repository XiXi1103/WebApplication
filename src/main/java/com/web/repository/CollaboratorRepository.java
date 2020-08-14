package com.web.repository;

import com.web.entity.Collaborator;
import com.web.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator,Integer> {
    Collaborator findCollaboratorByUserIdAndAndDocumentationId(int userId,int docId);

}
