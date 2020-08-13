package com.web.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Collaborator")
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "UserId", length = 20)
    public int userId;
    @Column(name = "DocumentationId", length = 20)
    public int documentationId;
    @Column(name = "permission")
    public int permission;
}