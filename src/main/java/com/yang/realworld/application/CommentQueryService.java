package com.yang.realworld.application;

import com.yang.realworld.application.data.CommentData;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import com.yang.realworld.infrastructure.mybatis.readservice.CommentReadService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentQueryService {

  private CommentReadService commentReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  @Autowired
  public CommentQueryService(CommentReadService commentReadService,
                             UserRelationshipQueryService userRelationshipQueryServicel) {
    this.commentReadService = commentReadService;
    this.userRelationshipQueryService = userRelationshipQueryServicel;
  }

  public Optional<CommentData> findById(String id, User user) {
    CommentData commentData = commentReadService.findById(id);
    if (commentData == null) {
      return Optional.empty();
    } else {
      commentData.getProfileData().setFollowing(
          userRelationshipQueryService.isUserFollowing(
              user.getId(), commentData.getProfileData().getId()));
    }
    return Optional.ofNullable(commentData);
  }

  public List<CommentData> findByArticleId(String id, User user) {
    return null;
  }
}
