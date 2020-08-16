package com.web.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "documentationModificationRecord")
public class DocumentModificationRecord {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public int id;
        @Column(name = "docId")
        public int docId;
        @Column(name = "userId")
        public int userId;
        @Column(name = "time")
        public Date time;
}
