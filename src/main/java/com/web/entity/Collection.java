package com.web.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Collection")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "UserId", length = 20)
    public int userId;
    @Column(name = "DocumentationId", length = 20)
    public int documentationId;
    @Column(name = "collect_time")
    public Date collect_time;
}