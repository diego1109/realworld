package com.yang.realworld.application;

import com.yang.realworld.application.data.UserData;
import com.yang.realworld.infrastructure.mybatis.readservice.UserReadService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {
  private UserReadService userReadService;

  @Autowired
  public UserQueryService(UserReadService userReadService) {
    this.userReadService = userReadService;
  }

  public Optional<UserData> findById(String id){
    return Optional.ofNullable(userReadService.findById(id));
  }
}
