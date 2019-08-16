package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;

import com.yang.realworld.application.ArticleQueryService;
import com.yang.realworld.application.CommentQueryService;
import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.application.data.CommentData;
import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.domain.Comment;
import com.yang.realworld.domain.CommentRepository;
import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.infrastructure.mybatis.ArticleReadService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.HashMap;
import java.util.Optional;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentsApi.class)
@RunWith(SpringRunner.class)
public class CommentsApiTest extends TestWithCurrentUser {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private ArticleRepository articleRepository;

  @MockBean
  private CommentQueryService commentQueryService;

  @MockBean
  private CommentRepository commentRepository;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  public void should_create_comment_succeed() {
    Article article = new Article("new title", "new description", "new body",
                                  new String[]{"java", "spring", "jpg"}, user.getId(),
                                  new DateTime());
    when(articleRepository.findBySlug(eq(article.getSlug()))).thenReturn(Optional.of(article));

    ProfileData profileData = new ProfileData(user.getId(), user.getUserName(), user.getBio(),
                                              user.getImage(), false);
    CommentData commentData = new CommentData("commentId", "commentBody", article.getId(),
                                              DateTime.now(), null, profileData);

    when(commentQueryService.findById(any(), any())).thenReturn(Optional.of(commentData));
    given()
        .header("Authorization", "Token " + token)
        .contentType("application/json")
        .body(new HashMap<String, Object>() {{
          put("comment", new HashMap<String, Object>() {{put("body", "comment content");}});
        }})
        .when()
        .post("/articles/{slug}/comments", article.getSlug())
        .then()
        .statusCode(201)
        .body("comment.body", equalTo(commentData.getBody()));
  }
}