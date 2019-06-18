package com.yang.realworld.infrastructure.mybatis.readservice;

import com.yang.realworld.application.data.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserReadService {
  UserData findById(@Param("id") String id);
}
