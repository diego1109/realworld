package com.yang.realworld.domain;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

@NoArgsConstructor
@Getter
@ToString
public class Comment {
  private String id;
  private String body;
  private String userId;
  private String articleId;
  private DateTime createdAt;

  public Comment(String body, String userId, String articleId) {
    this.id = UUID.randomUUID().toString();
    this.body = body;
    this.userId = userId;
    this.articleId = articleId;
    this.createdAt = DateTime.now();
  }
}
