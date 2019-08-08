package com.yang.realworld.domain.article;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;


@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class Article {

  private String userId;
  private String id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private List<Tag> tags;
  private DateTime createdAt;
  private DateTime updatedAt;

  public Article(String title, String description, String body, String[] tags, String userId) {
    this(title, description, body, tags, userId, new DateTime());
  }



  public Article(String title, String description, String body,
                 String[] tags,String userId, DateTime createAt) {
    this.id = UUID.randomUUID().toString();
    this.slug = toSlug(title);
    this.userId = userId;
    this.title = title;
    this.description = description;
    this.body = body;
    this.tags = Arrays.stream(tags).collect(toSet()).stream().map(Tag::new).collect(toList());
    this.createdAt = createAt;
    this.updatedAt = createAt;
  }

  public void update(String title, String description, String body) {
    if (!"".equals(title)) {
      this.title = title;
      this.slug = toSlug(title);
    }
    if (!"".equals(description)) {
      this.description = description;
    }
    if (!"".equals(body)) {
      this.body = body;
    }
    this.updatedAt = new DateTime();
  }

  private String toSlug(String title) {
    return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
  }
}
