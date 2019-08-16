package com.yang.realworld.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

@NoArgsConstructor
@Getter
@ToString
public class CommentData {
  private String id;
  private String body;
  @JsonIgnore
  private String articleId;
  private DateTime created_at;
  private DateTime updated_at;
  @JsonProperty("author")
  private ProfileData profileData;

  public CommentData(String id, String body, String articleId, DateTime created_at,
                     DateTime updated_at, ProfileData profileData) {
    this.id = id;
    this.body = body;
    this.articleId = articleId;
    this.created_at = created_at;
    this.updated_at = updated_at;
    this.profileData = profileData;
  }
}
