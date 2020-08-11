package com.web.repository;

import com.web.entity.Group;
import com.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {
    Group findGroupById(Integer id);
    List<Group> findByGroupName(String name);
}