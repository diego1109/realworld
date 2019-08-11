package com.yang.realworld.infrastructure.mybatis;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserRelationshipQueryService {

  boolean isUserFollowing(@Param("userId") String userId,
                          @Param("anotherUserId") String anotherUserId);

  Set<String> followAuthors(@Param("currentUserId") String currentUserId, @Param("userIds") List<String> userIds);
}
