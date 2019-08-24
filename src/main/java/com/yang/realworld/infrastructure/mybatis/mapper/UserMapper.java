package com.yang.realworld.infrastructure.mybatis.mapper;

import com.yang.realworld.domain.user.FollowRelation;
import com.yang.realworld.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

  User findById(@Param("id") String id);

  void insert(@Param("user") User user);

  void update(@Param("user") User user);

  User findByName(@Param("userName") String userName);

  User findByUsername(@Param("username") String username);

  User findByEmail(@Param("email") String email);

  void saveRelation(@Param("followRelation") FollowRelation followRelation);

  FollowRelation findRelation(@Param("userId") String userId, @Param("targetId") String targetId);


  void deleteRelation(@Param("relation") FollowRelation relation);
}


