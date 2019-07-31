package com.yang.realworld.domain.article;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository {
  void save(Article article);

  Optional<Article> findById(String id);

  Optional<Article> findBySlug(String slug);

  void remove(Article article);
}
