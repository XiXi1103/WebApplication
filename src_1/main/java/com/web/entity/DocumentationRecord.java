package com.web.entity;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "DocumentationRecord")
public class DocumentationRecord  implements Comparable<DocumentationRecord>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "UserId", length = 20)
    public int userId;
    @Column(name = "DocumentationId", length = 20)
    public int documentationId;
    @Column(name = "time")
    public Date time;
    @Override
    public int compareTo(DocumentationRecord documentationRecord) {
        return this.time.compareTo(documentationRecord.time);
    }
}
