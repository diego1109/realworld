package com.yang.realworld.infrastructure.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.yang.realworld.core.article.Article;
import com.yang.realworld.core.article.ArticleRepository;
import com.yang.realworld.core.user.User;
import com.yang.realworld.core.user.UserRepository;
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
    System.out.println("id:"+article.getId());
    articleRepository.save(article);
    Optional<Article> optional = articleRepository.findById(article.getId());
    assertThat(optional.isPresent(), is(true));
    Article fetched = optional.get();
    assertThat(fetched.getTitle(), is(article.getTitle()));
    assertThat(fetched.getBody(), not(""));
  }
}