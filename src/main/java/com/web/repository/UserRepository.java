package com.web.repository;

import com.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserById(Integer id);
    User findUserByUsername(String userName);
    List<User> findByUsername(String name);
    List<User> findByUsernameAndPassword(String name, String password);
    List<User> findByEmail(String email);
    List<User> findByUsernameAndEmail(String name, String email);
}