package com.yang.realworld.application;

import com.yang.realworld.domain.article.Article;
import com.yang.realworld.infrastructure.mybatis.ArticleFavoritesReadService;
import com.yang.realworld.infrastructure.mybatis.UserRelationshipQueryService;
import com.yang.realworld.infrastructure.repository.MyBatisUserRepository;
import com.yang.realworld.infrastructure.repository.MybatisArticleFavoriteRespository;
import com.yang.realworld.infrastructure.repository.MybatisArticleRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@MybatisTest
@Import({ArticleQueryService.class,
    MybatisArticleRepositoryTest.class,
    MyBatisUserRepository.class,
MybatisArticleFavoriteRespository.class})
public class ArticleQueryServiceTest {

  @Autowired
  private ArticleQueryService articleQueryService;

  @Autowired
  private ArticleFavoritesReadService articleFavoritesReadService;

  @Autowired
  private UserRelationshipQueryService userRelationshipQueryService;



  @Test
  public void should_get_article_by_slug_success(){
    Article article =
  }
}