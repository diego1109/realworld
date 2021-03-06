package com.yang.realworld.application;

import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.application.data.ArticleDataList;
import com.yang.realworld.application.data.ArticleFavoriteCount;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.infrastructure.mybatis.readservice.ArticleFavoritesReadService;
import com.yang.realworld.infrastructure.mybatis.readservice.ArticleReadService;
import com.yang.realworld.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleQueryService {

  private ArticleReadService articleReadService;
  private ArticleFavoritesReadService articleFavoritesReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  @Autowired
  public ArticleQueryService(
      ArticleReadService articleReadService,
      ArticleFavoritesReadService articleFavoritesReadService,
      UserRelationshipQueryService userRelationshipQueryService) {
    this.articleReadService = articleReadService;
    this.articleFavoritesReadService = articleFavoritesReadService;
    this.userRelationshipQueryService = userRelationshipQueryService;
  }


  public Optional<ArticleData> findBySlug(String slug, User user) {
    ArticleData articleData = articleReadService.findBySlug(slug);
    if (articleData == null) {
      return Optional.empty();
    } else {
      if (user != null) {
        FillExtraInfo(articleData.getId(), user, articleData);
      }
      return Optional.of(articleData);
    }
  }

  private void FillExtraInfo(String id, User user, ArticleData articleData) {
    articleData.setFavorited(articleFavoritesReadService.isUserFavorite(user.getId(), id));
    articleData.setFavoritesCount(articleFavoritesReadService.articleFavoriteCount(id));
    articleData.getProfileData().setFollowing(userRelationshipQueryService
                                                  .isUserFollowing(user.getId(),
                                                                   articleData.getProfileData()
                                                                       .getId()));
  }

  public Optional<ArticleData> findById(String id, User user) {
    return Optional.empty();
  }

  public ArticleDataList findRecentArticles(String tag, String author, String favoritedBy,
                                            Page page, User user) {
    List<String> articleIds = articleReadService.queryArticles(tag, author, favoritedBy, page);
    int articleCount = articleReadService.countArtile(tag, author, favoritedBy);
    if (articleIds.size() == 0) {
      return new ArticleDataList(new ArrayList<>(), articleCount);
    } else {
      List<ArticleData> articles = articleReadService.findArticles(articleIds);
      fillExtraInfo(articles, user);
      return new ArticleDataList(articles, articleCount);
    }
  }

  private void fillExtraInfo(List<ArticleData> articles, User user) {
    setFavoriteCount(articles);
    if (user != null) {
      setIsFavorite(articles, user);
      setIsFollowingAuthor(articles, user);
    }
  }

  private void setIsFollowingAuthor(List<ArticleData> articleData, User currentUser) {
    Set<String> followingAuthors = userRelationshipQueryService.followAuthors(currentUser.getId(),
                                                                              articleData.stream()
                                                                                  .map(item -> item
                                                                                      .getProfileData()
                                                                                      .getId())
                                                                                  .collect(
                                                                                      Collectors
                                                                                          .toList()));
    articleData.forEach(item -> {
      if (followingAuthors.contains(item.getProfileData().getId())){
        item.getProfileData().setFollowing(true);
      }
    });
  }

  private void setIsFavorite(List<ArticleData> articleData, User currentUser) {
    Set<String> favoritedArticleIds = articleFavoritesReadService
        .userFavorites(articleData.stream().map(item -> item.getId()).collect(Collectors.toList()),
                       currentUser);
    articleData.forEach(item -> {
      if (favoritedArticleIds.contains(item.getId())) {
        item.setFavorited(true);
      }
    });
  }

  private void setFavoriteCount(List<ArticleData> articles) {
    List<ArticleFavoriteCount> favoriteCounts = articleFavoritesReadService.articlesFavoriteCount(
        articles.stream().map(articleData -> articleData.getId()).collect(Collectors.toList()));
    Map<String,Integer> countMap = new HashMap<>();
    favoriteCounts.forEach(item->{countMap.put(item.getId(),item.getCount());});
    articles.forEach(articleData -> articleData.setFavoritesCount(countMap.get(articleData.getId())));
  }


  public ArticleDataList findUserFeed(User user, Page page) {
    List<String> followedUsers = userRelationshipQueryService.followedAuthors(user.getId());
    if (followedUsers.size() == 0) {
      return new ArticleDataList(new ArrayList<>(), 0);
    } else {
      List<ArticleData> articles = articleReadService.findArticlesOfAuthors(followedUsers, page);
      fillExtraInfo(articles, user);
      int count = articleReadService.countFeedSize(followedUsers);
      return new ArticleDataList(articles, count);
    }

  }
}






















