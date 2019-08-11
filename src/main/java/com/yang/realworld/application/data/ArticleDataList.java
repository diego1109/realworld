package com.yang.realworld.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ArticleDataList {

  @JsonProperty("articles")
  private List<ArticleData> articleDatas;
  @JsonProperty("ArticleCount")
  private int count;

  public ArticleDataList(
      List<ArticleData> articleDatas, int count) {
    this.articleDatas = articleDatas;
    this.count = count;
  }
}
