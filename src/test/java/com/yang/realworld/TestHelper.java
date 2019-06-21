package com.yang.realworld;

import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.core.article.Article;
import com.yang.realworld.core.user.User;
import java.util.ArrayList;
import java.util.Arrays;
import org.joda.time.DateTime;

public class TestHelper {
  public static ArticleData articleDataFixture(String seed, User user) {
    DateTime now = new DateTime();
    return new ArticleData(
        seed + "id",
        "title-" + seed,
        "title " + seed,
        "desc " + seed,
        "body " + seed, false, 0, now, now, new ArrayList<>(),
        new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), false));
  }

  public static ArticleData getArticleDataFromArticleAndUser(Article article, User user) {
    return new ArticleData(
        article.getId(),
        article.getSlug(),
        article.getTitle(),
        article.getDescription(),
        article.getBody(),
        false,
        0,
        article.getCreatedAt(),
        article.getUpdatedAt(),
        Arrays.asList("joda"),
        new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), false));
  }
}

