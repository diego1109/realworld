package com.yang.realworld.infrastructure.mybatis.readservice;

import com.yang.realworld.application.data.CommentData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CommentReadService {

  CommentData findById(@Param("id") String id);

  List<CommentData> findByArticleId(@Param("articleId") String articleId);
}
