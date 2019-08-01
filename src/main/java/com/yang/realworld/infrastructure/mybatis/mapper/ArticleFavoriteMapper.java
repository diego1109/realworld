package com.yang.realworld.infrastructure.mybatis.mapper;

import com.yang.realworld.domain.favorite.ArticleFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ArticleFavoriteMapper {

  ArticleFavorite find(@Param("articleId") String articleId, @Param("userId") String userId);

  void insert(@Param("articleFavorite") ArticleFavorite articleFavorite);

  void delete(@Param("articleFavorite") ArticleFavorite articleFavorite);
}
