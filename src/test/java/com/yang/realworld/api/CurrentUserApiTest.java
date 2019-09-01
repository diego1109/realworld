package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.yang.realworld.JacksonCustomizations;
import com.yang.realworld.api.security.WebSecurityConfig;
import com.yang.realworld.application.UserQueryService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CurrentUserApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class CurrentUserApiTest extends TestWithCurrentUser{

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserQueryService userQueryService;

  @Before
  public void setUp() throws Exception{
    super.setUp();
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  public void should_get_current_user_with_token(){
    Mockito.when(userQueryService.findById(any())).thenReturn(Optional.of(userData));
    given()
        .header("Authorization", "Token " + token)
        .contentType(ContentType.JSON)
        .when()
        .get("/user")
        .then()
        .statusCode(200)
        .body("user.email",equalTo(userData.getEmail()))
        .body("user.userName",equalTo(userData.getUserName()))
        .body("user.bio",equalTo(userData.getBio()))
        .body("user.image",equalTo(userData.getImage()))
        .body("user.token",equalTo(token));
  }

  @Test
  public void should_update_user_profile_succeed(){
    String newEmail = "newEamil@qq.com";
    String newBio = "newBio";
    String newUserName = "newUserName";

    Map<String,Object> param = new HashMap<String,Object>(){{
      put("user",new HashMap<String,Object>(){{
        put("email",newEmail);
        put("username",newUserName);
        put("bio",newBio);
      }});
    }};

    Mockito.when(userRepository.findByUserName(eq(newUserName))).thenReturn(Optional.empty());
    Mockito.when(userRepository.findByEmail(eq(newEmail))).thenReturn(Optional.empty());

    Mockito.when(userQueryService.findById(eq(user.getId()))).thenReturn(Optional.of(userData));

    given()
        .contentType(ContentType.JSON)
        .header("Authorization", "Token " + token)
        .body(param)
        .when()
        .put("/user")
        .then()
        .statusCode(200);
  }

}





























