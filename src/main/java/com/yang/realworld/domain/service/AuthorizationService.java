package com.yang.realworld.domain.service;

import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.user.User;

public class AuthorizationService {

  public static boolean canWriteArticle(Article article, User user){
    return user.getId().equals(article.getUserId());
  }
}
