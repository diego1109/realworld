package com.yang.realworld.infrastructure.mybatis.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import java.util.Optional;
import org.junit.Before;
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
@Import({MyBatisArticleRepository.class, MyBatisUserRepository.class})
public class MybatisArticleRepositoryTest {

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private UserRepository userRepository;

  private Article article;

  @Before
  public void setUp() throws Exception {
    User user = new User("1027@qq.com", "diego", "123", "", "default");
    userRepository.save(user);
    article = new Article("test", "desc", "body", new String[]{"java", "spring"}, user.getId());
  }

  @Test
  public void should_create_and_fetch_article_success() {
    System.out.println(article);
    articleRepository.save(article);
    Optional<Article> optional = articleRepository.findById(article.getId());
    assertThat(optional.isPresent(), is(true));
    Article fetched = optional.get();
    assertThat(fetched.getId(), is(article.getId()));
    assertThat(fetched.getUserId(), is(article.getUserId()));
    assertThat(fetched.getSlug(), is(article.getSlug()));
    assertThat(fetched.getTags().size(), is(article.getTags().size()));
    assertThat(fetched.getDescription(), is(article.getDescription()));
    assertThat(fetched.getTitle(), is(article.getTitle()));
    assertThat(fetched.getBody(), is(article.getBody()));
  }

  @Test
  public void should_find_article_by_slug_succeed(){
    articleRepository.save(article);
    Optional<Article> optional = articleRepository.findBySlug(article.getSlug());
    Article fetched = optional.get();
    assertThat(fetched.getId(), is(article.getId()));
    assertThat(fetched.getUserId(), is(article.getUserId()));
    assertThat(fetched.getSlug(), is(article.getSlug()));
    assertThat(fetched.getTags().size(), is(article.getTags().size()));
    assertThat(fetched.getDescription(), is(article.getDescription()));
    assertThat(fetched.getTitle(), is(article.getTitle()));
    assertThat(fetched.getBody(), is(article.getBody()));
  }

  @Test
  public void should_delete_article_succeed() {
    articleRepository.save(article);
    articleRepository.remove(article);
    Optional<Article> optional = articleRepository.findById(article.getId());
    assertThat(optional.isPresent(), is(false));
  }
}