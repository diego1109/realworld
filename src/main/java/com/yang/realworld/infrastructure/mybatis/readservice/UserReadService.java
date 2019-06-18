package com.yang.realworld.infrastructure.mybatis.readservice;

import com.yang.realworld.application.data.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserReadService {
  UserData findById(@Param("id") String id);

  UserData findByUserName(@Param("userName") String userName);
}
