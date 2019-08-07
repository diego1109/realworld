package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
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
import java.util.HashMap;
import java.util.Map;
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

  private Article article;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mvc);
    DateTime time = new DateTime();
    article = new Article("New Article", "Desc", "Body",
                          new String[]{"java", "spring", "jpg"}, user.getId(), time);

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

  @Test
  public void should_update_article_succeed() throws Exception {
    Map<String, Object> updateParam = new HashMap<String, Object>() {{
      put("article", new HashMap<String, Object>() {{
        put("title", "update title"); put("description", "update description");
        put("body", "update body");
      }});
    }};
    when(articleRepository.findBySlug(eq(article.getSlug()))).thenReturn(Optional.of(article));
    ArticleData articleData = TestHelper.getArticleDataFromArticleAndUser(article, user);
    when(articleQueryService.findBySlug(eq(article.getSlug()), eq(user)))
        .thenReturn(Optional.of(articleData));

    given()
        .contentType("application/json")
        .header("Authorization", "Token " + token)
        .body(updateParam)
        .when()
        .put("/articles/{slug}", article.getSlug())
        .then()
        .statusCode(200)
        .body("article.slug", equalTo(articleData.getSlug()));
  }

  @Test
  public void should_delete_article_succeed() {
    when(articleRepository.findBySlug(eq(article.getSlug()))).thenReturn(Optional.of(article));
    given()
        .header("Authorization", "Token " + token)
        .when()
        .delete("/articles/{slug}",article.getSlug())
        .then()
        .statusCode(204);
  }

}