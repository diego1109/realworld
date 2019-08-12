package com.yang.realworld.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.application.data.ArticleDataList;
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
import org.junit.Assert;
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
  private ArticleQueryService queryService;

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
  private User user;
  private ArticleFavorite articleFavorite;
  private FollowRelation followRelation;

  @Before
  public void setUp() {
    user = new User("1234@qq.com", "haha", "234", "", "default");
    userRepository.save(user);
    article = new Article("test", "desc", "body", new String[]{"java", "spring"},
                          user.getId());
    articleRepository.save(article);
  }

  @Test
  public void should_get_article_by_slug_success(){
    loginUser = new User("1027@qq.com", "diego", "123", "", "default");
    articleFavorite = new ArticleFavorite(article.getId(), loginUser.getId());
    articleFavoriteRepository.save(articleFavorite);
    followRelation = new FollowRelation(loginUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    Optional<ArticleData> optional = queryService.findBySlug(article.getSlug(), loginUser);
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
    assertThat(profileData.getId(), is(user.getId()));
    assertThat(profileData.getUsername(), is(user.getUserName()));
    assertThat(profileData.getBio(), is(user.getBio()));
    assertThat(profileData.getImage(), is(user.getImage()));
    assertThat(profileData.isFollowing(), is(true));
  }

  @Test
  public void should_get_default_articles() throws Exception {
    Article anotherArticle = new Article("another article", "description", "article body",
                                         new String[]{"test",}, user.getId());
    articleRepository.save(anotherArticle);

    ArticleDataList recentArticles = queryService
        .findRecentArticles(null, null, null, new Page(), user);
    assertThat(recentArticles.getCount(), is(2));
    assertThat(recentArticles.getArticleDatas().size(), is(2));
    assertThat(recentArticles.getArticleDatas().get(0).getId(), is(article.getId()));

    ArticleDataList nodatas = queryService
        .findRecentArticles(null, null, null, new Page(2, 10), user);
    assertThat(nodatas.getCount(), is(2));
    assertThat(nodatas.getArticleDatas().size(), is(0));
  }

  @Test
  public void should_query_articles_by_author() {
    User anotherUser = new User("another@qq.com", "another", "123", "", "");
    userRepository.save(anotherUser);
    Article anotherArticle = new Article("another article", "description", "article body",
                                         new String[]{"test",}, anotherUser.getId());
    articleRepository.save(anotherArticle);
    ArticleDataList recentArticles = queryService
        .findRecentArticles(null, user.getUserName(), null, new Page(), user);
    assertThat(recentArticles.getCount(), is(1));
    assertThat(recentArticles.getArticleDatas().size(), is(1));
  }

  @Test
  public void should_query_article_by_favorite() {
    User anotherUser = new User("another@qq.com", "another", "123", "", "");
    userRepository.save(anotherUser);

    ArticleFavorite articleFavorite = new ArticleFavorite(article.getId(), anotherUser.getId());
    articleFavoriteRepository.save(articleFavorite);

    ArticleDataList recentArticles = queryService
        .findRecentArticles(null, null, anotherUser.getUserName(), new Page(), anotherUser);
    assertThat(recentArticles.getCount(), is(1));
    assertThat(recentArticles.getArticleDatas().size(), is(1));
    ArticleData articleData = recentArticles.getArticleDatas().get(0);
    assertThat(articleData.getId(), is(article.getId()));
  }

  @Test
  public void should_query_article_by_tag() {
    Article anotherArticle = new Article("another article", "description", "article body",
                                         new String[]{"test",}, user.getId());
    articleRepository.save(anotherArticle);

    ArticleDataList recentArticles = queryService
        .findRecentArticles("spring", null, null, new Page(), user);
    assertThat(recentArticles.getCount(), is(1));
    assertThat(recentArticles.getArticleDatas().size(), is(1));
    ArticleData articleData = recentArticles.getArticleDatas().get(0);
    assertThat(articleData.getId(), is(article.getId()));
  }

  @Test
  public void should_show_follow_if_user_follow_author() {
    User anotherUser = new User("another@qq.com", "another", "123", "", "");
    userRepository.save(anotherUser);

    FollowRelation followRelation = new FollowRelation(anotherUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    ArticleDataList recentArticles = queryService
        .findRecentArticles(null, null, null, new Page(), anotherUser);
    Assert.assertThat(recentArticles.getCount(), is(1));
    ArticleData articleData = recentArticles.getArticleDatas().get(0);
    Assert.assertThat(articleData.getProfileData().isFollowing(), is(true));
  }

  @Test
  public void should_get_user_feed(){
    User anotherUser = new User("another@qq.com", "another", "123", "", "");
    userRepository.save(anotherUser);

    FollowRelation followRelation = new FollowRelation(anotherUser.getId(), user.getId());
    userRepository.saveRelation(followRelation);

    ArticleDataList userFeed = queryService.findUserFeed(user,new Page());
    assertThat(userFeed.getCount(),is(0));

    ArticleDataList anotherUserFeed = queryService.findUserFeed(anotherUser, new Page());
    Assert.assertThat(anotherUserFeed.getCount(), is(1));
    ArticleData articleData = anotherUserFeed.getArticleDatas().get(0);
    Assert.assertThat(articleData.getProfileData().isFollowing(), is(true));
  }

}