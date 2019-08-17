package com.yang.realworld.infrastructure.mybatis.mapper;

import com.yang.realworld.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CommentMapper {

  void insert(@Param("comment") Comment comment);

  Comment findById(@Param("articleId") String articleId, @Param("id") String id);

  void delete(@Param("comment") Comment comment);
}
