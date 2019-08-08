package com.yang.realworld.infrastructure.mybatis.mapper;

import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ArticleMapper {

  Article findById(@Param("id") String id);

  void update(@Param("artilcle") Article article);

  Boolean findTag(@Param("tagName") String tagName);

  void insertTag(@Param("tag") Tag tag);

  void insertArticleTagRelation(@Param("articleId") String articleId,@Param("tagId") String tagId);

  void insert(@Param("article") Article article);

  Article findBySlug(@Param("slug") String slug);

  void remove(@Param("articleId") String articleId);
}
