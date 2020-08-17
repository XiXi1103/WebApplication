package com.web.repository;

import com.web.entity.Collaborator;
import com.web.entity.DocumentationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface DocumentationRecordRepository extends JpaRepository<DocumentationRecord,Integer> {
    ArrayList<DocumentationRecord> findDocumentationRecordByDocumentationId(int docId);
    ArrayList<DocumentationRecord> findDocumentationRecordByUserId(int userId);
    DocumentationRecord findDocumentationRecordByUserIdAndDocumentationId(int userId,int docId);
}
