package com.yang.realworld.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;



@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData {
  private String id;
  private String slug;
  private String title;
  private String description;
  private String body;
  //是否点赞
  private Boolean favorited;
  //点赞数
  private int favoritesCount;
  private DateTime createdAt;
  private DateTime updatedAt;
  private List<String> tagList;
  //个人信息
  @JsonProperty("author")
  private ProfileData profileData;

}
