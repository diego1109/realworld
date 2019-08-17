package com.yang.realworld.infrastructure.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.yang.realworld.domain.favorite.ArticleFavorite;
import com.yang.realworld.domain.favorite.ArticleFavoriteRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(MybatisArticleFavoriteRepository.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@MybatisTest
public class MybatisArticleFavoriteRepositoryTest {

  @Autowired
  private ArticleFavoriteRepository articleFavoriteRepository;

  ArticleFavorite articleFavorite;

  @Before
  public void setUp() {
    articleFavorite = new ArticleFavorite("userId", "ArticleId");
    articleFavoriteRepository.save(articleFavorite);
  }

  @Test
  public void should_save_article_favorite_succeeds() {

    Optional<ArticleFavorite> optional = articleFavoriteRepository
        .find(articleFavorite.getArticleId(), articleFavorite.getUserId());

    assertThat(optional.isPresent(), is(true));
    ArticleFavorite retched = optional.get();
    assertThat(retched.getArticleId(), is(articleFavorite.getArticleId()));
    assertThat(retched.getUserId(), is(retched.getUserId()));
  }

  @Test
  public void should_delete_article_favorite_succeeds() {
    articleFavoriteRepository.remove(articleFavorite);
    Optional<ArticleFavorite> optional = articleFavoriteRepository
        .find(articleFavorite.getArticleId(), articleFavorite.getUserId());
    assertThat(optional.isPresent(), is(false));
  }
}