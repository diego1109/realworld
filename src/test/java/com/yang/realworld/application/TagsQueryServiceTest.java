package com.yang.realworld.application;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.infrastructure.repository.MyBatisArticleRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({MyBatisArticleRepository.class, TagsQueryService.class})
public class TagsQueryServiceTest {

  @Autowired
  private ArticleRepository articleRepository;
  @Autowired
  private TagsQueryService tagsQueryService;

  @Test
  public void should_get_tags_succeed() {
    articleRepository
        .save(new Article("test title", "test description", "test body", new String[]{"java"},
            "user-id"));
    List<String> tags = tagsQueryService.allTags();
    assertThat(tags.size(), is(1));
    assertThat(tags.contains("java"), is(true));
  }
}