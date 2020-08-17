package com.web.repository;

import ch.qos.logback.core.boolex.EvaluationException;
import com.web.entity.Collaborator;
import com.web.entity.DocumentationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface DocumentationRecordRepository extends JpaRepository<DocumentationRecord,Integer> {
    ArrayList<DocumentationRecord> findDocumentationRecordByDocumentationId(int docId);
    //@Query(value="select * from  DocumentationRecord order by 'time' desc", nativeQuery=true)
    ArrayList<DocumentationRecord> findDocumentationRecordByUserId(int userId);
    DocumentationRecord findDocumentationRecordByUserIdAndDocumentationId(int userId,int docId);
}
