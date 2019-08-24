package com.yang.realworld.application;

import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.application.data.UserData;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.infrastructure.mybatis.readservice.UserReadService;
import com.yang.realworld.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileQueryService {

  private UserReadService userReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  @Autowired
  public ProfileQueryService(
      UserReadService userReadService, UserRelationshipQueryService userRelationshipQueryService) {
    this.userReadService = userReadService;
    this.userRelationshipQueryService = userRelationshipQueryService;
  }

  public Optional<ProfileData> findByUsername(String userName, User current) {
    UserData userData = userReadService.findByUserName(userName);
    if (userData == null) {
      return Optional.empty();
    } else {
      ProfileData profileData = new ProfileData(userData.getId(), userData.getUserName(),
          userData.getBio(), userData.getImage(),
          userRelationshipQueryService.isUserFollowing(current.getId(), userData.getId()));
      return Optional.of(profileData);
    }
  }
}
