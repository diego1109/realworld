package com.yang.realworld.infrastructure.mybatis;

import com.yang.realworld.application.data.ArticleData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ArticleReadService {

  ArticleData findBySlug(@Param("slug") String slug);
}
