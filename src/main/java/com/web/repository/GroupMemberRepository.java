package com.web.repository;

import com.web.entity.Group;
import com.web.entity.GroupMember;
import com.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Integer> {
    List<User> findUserById(Integer id);
    List<Group> findGroupById(Integer id);
    GroupMember findByUserIdAndGroupId(Integer userId,Integer groupId);
}
