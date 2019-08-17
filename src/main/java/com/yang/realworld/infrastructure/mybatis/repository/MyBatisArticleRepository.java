package com.yang.realworld.infrastructure.mybatis.repository;

import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.article.Tag;
import com.yang.realworld.infrastructure.mybatis.mapper.ArticleMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MyBatisArticleRepository implements ArticleRepository {

  private ArticleMapper articleMapper;

  @Autowired
  public MyBatisArticleRepository(
      ArticleMapper articleMapper) {
    this.articleMapper = articleMapper;
  }

  @Override
  @Transactional
  public void save(Article article) {
    if (articleMapper.findById(article.getId()) == null) {
      createNew(article);
    } else {
      articleMapper.update(article);
    }
  }

  private void createNew(Article article) {
    for (Tag tag : article.getTags()) {
      if (!articleMapper.findTag(tag.getName())) {
        articleMapper.insertTag(tag);
      }
      articleMapper.insertArticleTagRelation(article.getId(), tag.getId());
    }
    articleMapper.insert(article);
  }

  @Override
  public Optional<Article> findById(String id) {
    return Optional.ofNullable(articleMapper.findById(id));
  }

  @Override
  public Optional<Article> findBySlug(String slug) {
    return Optional.ofNullable(articleMapper.findBySlug(slug));
  }

  @Override
  public void remove(Article article) {
    if (articleMapper.findById(article.getId()) != null) {
      articleMapper.remove(article.getId());
    }
  }
}
