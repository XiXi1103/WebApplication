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
    List<GroupMember> findGroupMemberByUserId(Integer id);
    List<GroupMember> findGroupMemberByGroupId(Integer id);
    GroupMember findGroupMemberByUserIdAndGroupId(Integer userId,Integer groupId);
}
