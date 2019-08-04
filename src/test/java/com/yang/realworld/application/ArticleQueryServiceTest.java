package com.yang.realworld.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.favorite.ArticleFavorite;
import com.yang.realworld.domain.favorite.ArticleFavoriteRepository;
import com.yang.realworld.domain.user.FollowRelation;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import com.yang.realworld.infrastructure.mybatis.ArticleFavoritesReadService;
import com.yang.realworld.infrastructure.mybatis.UserRelationshipQueryService;
import com.yang.realworld.infrastructure.repository.MyBatisArticleRepository;
import com.yang.realworld.infrastructure.repository.MyBatisUserRepository;
import com.yang.realworld.infrastructure.repository.MybatisArticleFavoriteRepository;
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
@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({ArticleQueryService.class,
    MyBatisArticleRepository.class,
    MyBatisUserRepository.class,
    MybatisArticleFavoriteRepository.class})
public class ArticleQueryServiceTest {

  @Autowired
  private ArticleQueryService articleQueryService;

  @Autowired
  private ArticleFavoritesReadService articleFavoritesReadService;

  @Autowired
  private UserRelationshipQueryService userRelationshipQueryService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private ArticleFavoriteRepository articleFavoriteRepository;

  private Article article;
  private User loginUser;
  private User articleUser;
  private ArticleFavorite articleFavorite;
  private FollowRelation followRelation;

  @Before
  public void setUp() {
    loginUser = new User("1027@qq.com", "diego", "123", "", "default");
    articleUser = new User("1234@qq.com", "haha", "234", "", "default");
    userRepository.save(articleUser);
    article = new Article("test", "desc", "body", new String[]{"java", "spring"},
                          articleUser.getId());
    articleRepository.save(article);
  }

  @Test
  public void should_get_article_by_slug_success(){
    articleFavorite = new ArticleFavorite(article.getId(), loginUser.getId());
    articleFavoriteRepository.save(articleFavorite);
    followRelation = new FollowRelation(loginUser.getId(), articleUser.getId());
    userRepository.saveRelation(followRelation);

    Optional<ArticleData> optional = articleQueryService.findBySlug(article.getSlug(), loginUser);
    assertThat(optional.isPresent(), is(true));

    ArticleData refetch = optional.get();
    assertThat(refetch.getId(), is(article.getId()));
    assertThat(refetch.getSlug(), is(article.getSlug()));
    assertThat(refetch.getTitle(), is(article.getTitle()));
    assertThat(refetch.getBody(), is(article.getBody()));
    assertThat(refetch.getDescription(), is(article.getDescription()));
    assertThat(refetch.getFavorited(), is(true));
    assertThat(refetch.getFavoritesCount(), is(1));
    assertThat(refetch.getTagList().size(), is(2));

    ProfileData profileData = refetch.getProfileData();
    assertThat(profileData.getId(), is(articleUser.getId()));
    assertThat(profileData.getUsername(), is(articleUser.getUserName()));
    assertThat(profileData.getBio(), is(articleUser.getBio()));
    assertThat(profileData.getImage(), is(articleUser.getImage()));
    assertThat(profileData.isFollowing(), is(true));
  }
}