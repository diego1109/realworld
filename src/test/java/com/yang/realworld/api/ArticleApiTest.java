package com.yang.realworld.api;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.yang.realworld.JacksonCustomizations;
import com.yang.realworld.TestHelper;
import com.yang.realworld.api.security.WebSecurityConfig;
import com.yang.realworld.application.ArticleQueryService;
import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class ArticleApiTest extends TestWithCurrentUser {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ArticleQueryService articleQueryService;

  @MockBean
  private ArticleRepository articleRepository;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  public void should_read_article_success() throws Exception {
    String slug = "test-new-article";
    DateTime time = new DateTime();
    Article article = new Article("Test New Article", "Desc", "Body", new String[]{"java", "spring", "jpg"}, user.getId(), time);
    ArticleData articleData = TestHelper.getArticleDataFromArticleAndUser(article, user);
    when(articleQueryService.findBySlug(eq(slug), eq(null))).thenReturn(Optional.of(articleData));

    RestAssuredMockMvc.when()
        .get("/articles/{slug}", slug)
        .then()
        .statusCode(200)
        .body("article.slug", equalTo(slug))
        .body("article.body", equalTo(articleData.getBody()))
        .body("article.createdAt", equalTo(ISODateTimeFormat.dateTime().withZoneUTC().print(time)));

  }
}