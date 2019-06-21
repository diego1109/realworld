package com.yang.realworld.infrastructure.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserRelationshipQueryService {

  boolean isUserFollowing(@Param("userId") String userId,
                          @Param("anotherUserId") String anotherUserId);
}
