package com.yang.realworld.infrastructure.repository;

import com.yang.realworld.domain.favorite.ArticleFavorite;
import com.yang.realworld.domain.favorite.ArticleFavoriteRepository;
import com.yang.realworld.infrastructure.mybatis.mapper.ArticleFavoriteMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisArticleFavoriteRepository implements ArticleFavoriteRepository {

  private ArticleFavoriteMapper articleFavoriteMapper;

  @Autowired
  public MybatisArticleFavoriteRepository(
      ArticleFavoriteMapper articleFavoriteMapper) {
    this.articleFavoriteMapper = articleFavoriteMapper;
  }

  @Override
  public void save(ArticleFavorite articleFavorite) {
    if (articleFavoriteMapper.find(articleFavorite.getArticleId(), articleFavorite.getUserId())
        == null){
      articleFavoriteMapper.insert(articleFavorite);
    }
  }

  @Override
  public Optional<ArticleFavorite> find(String articleId, String userId) {
    return Optional.ofNullable(articleFavoriteMapper.find(articleId,userId));
  }

  @Override
  public void remove(ArticleFavorite articleFavorite) {
    if(articleFavoriteMapper.find(articleFavorite.getArticleId(),articleFavorite.getUserId())!=null){
      articleFavoriteMapper.delete(articleFavorite);
    }
  }
}
