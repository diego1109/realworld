package com.yang.realworld.domain.favorite;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleFavoriteRepository {

  void save(ArticleFavorite articleFavorite);

  Optional<ArticleFavorite> find(String articleId,String userId);

  void remove(ArticleFavorite favorite);
}
