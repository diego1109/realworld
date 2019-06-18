package com.yang.realworld.application.data;

import lombok.Getter;

@Getter
public class UserWithToken {
  private String email;
  private String userName;
  private String bio;
  private String image;
  private String token;

  public UserWithToken(UserData userData,
                       String token) {
    this.email = userData.getEmail();
    this.userName = userData.getUserName();
    this.bio = userData.getBio();
    this.image = userData.getImage();
    this.token = token;
  }
}
