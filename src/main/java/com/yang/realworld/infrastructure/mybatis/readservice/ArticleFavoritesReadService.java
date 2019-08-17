package com.yang.realworld.infrastructure.mybatis.readservice;

import com.yang.realworld.application.data.ArticleFavoriteCount;
import com.yang.realworld.domain.user.User;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ArticleFavoritesReadService {

  Boolean isUserFavorite(@Param("userId") String userId, @Param("articleId") String articleId);

  int articleFavoriteCount(@Param("articleId") String articleId);

  List<ArticleFavoriteCount> articlesFavoriteCount(@Param("articleIds") List<String> articleIds);

  Set<String> userFavorites(@Param("articleIds") List<String> articleIds, @Param("currentUser") User currentUser);
}
