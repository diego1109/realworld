package com.yang.realworld.infrastructure.mybatis.mapper;

import com.yang.realworld.core.user.User;
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

  User findByEmail(@Param("email") String email);
}


