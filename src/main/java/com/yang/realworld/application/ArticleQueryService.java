package com.yang.realworld.application;

import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.core.user.User;
import com.yang.realworld.infrastructure.mybatis.ArticleFavoritesReadService;
import com.yang.realworld.infrastructure.mybatis.ArticleReadService;
import com.yang.realworld.infrastructure.mybatis.UserRelationshipQueryService;
import java.util.Optional;
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
}
