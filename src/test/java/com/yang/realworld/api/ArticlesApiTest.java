package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

import com.yang.realworld.JacksonCustomizations;
import com.yang.realworld.TestHelper;
import com.yang.realworld.api.security.WebSecurityConfig;
import com.yang.realworld.application.ArticleQueryService;
import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(ArticlesApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class ArticlesApiTest extends TestWithCurrentUser {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ArticleRepository articleRepository;

  @MockBean
  private ArticleQueryService articleQueryService;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mvc);
  }

  private HashMap<String, Object> prepareParam(final String title, final String description, final String body, final String[] tagList) {
    return new HashMap<String, Object>() {{
      put("article", new HashMap<String, Object>() {{
        put("title", title);
        put("description", description);
        put("body", body);
        put("tagList", tagList);
      }});
    }};
  }

  @Test
  public void should_get_Articles_succeed(){

  }

  @Test
  public void create_article_by_params_succeed() {

    Article article = new Article("new title", "new description", "new body",
                                  new String[]{"java", "spring", "jpg"}, user.getId(), new DateTime());
    ArticleData articleData = TestHelper.getArticleDataFromArticleAndUser(article, user);

    Mockito.when(articleQueryService.findById(any(),any())).thenReturn(Optional.of(articleData));

    given()
        .contentType(ContentType.JSON)
        .body(generateCreateArticleParams())
        .header("Authorization", "Token " + token)
        .when()
        .post("/articles")
        .then()
        .statusCode(201)
        .body("article.title", equalTo(article.getTitle()))
        .body("article.body", equalTo(article.getBody()))
        .body("article.description", equalTo(article.getDescription()));
  }

  private Map<String, Object> generateCreateArticleParams() {
    return new HashMap<String, Object>() {{
      put("article", new HashMap<String, Object>() {{
        put("title", "new title");
        put("description", "new description");
        put("body", "new body");
        put("tagList",asList("java","Spring"));
      }});
    }};
  }
}