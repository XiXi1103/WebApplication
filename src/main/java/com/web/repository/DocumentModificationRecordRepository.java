package com.web.repository;

import com.web.entity.DocumentModificationRecord;
import com.web.entity.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentModificationRecordRepository extends JpaRepository<DocumentModificationRecord,Integer> {
    List<DocumentModificationRecord> findDocumentModificationRecordsByDocId(int docId);
}
