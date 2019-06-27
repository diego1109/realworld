package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.yang.realworld.api.security.WebSecurityConfig;
import com.yang.realworld.application.UserQueryService;
import com.yang.realworld.application.data.UserData;
import com.yang.realworld.core.service.JwtService;
import com.yang.realworld.core.user.User;
import com.yang.realworld.core.user.UserRepository;
import com.yang.realworld.infrastructure.mybatis.readservice.UserReadService;
import com.yang.realworld.infrastructure.repository.MyBatisUserRepository;
import com.yang.realworld.infrastructure.service.DefaultJwtService;
import com.yang.realworld.infrastructure.service.NaiveEncryptService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersApi.class)
@Import({MyBatisUserRepository.class, UserQueryService.class, NaiveEncryptService.class,
    DefaultJwtService.class, WebSecurityConfig.class})
public class UsersApiTest {

  @Autowired
  private MockMvc mvc;
  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserReadService userReadService;
  private String defaultAvatar;

  @MockBean
  private JwtService jwtService;

  @Before
  public void setUp() throws Exception {
    RestAssuredMockMvc.mockMvc(mvc);
    defaultAvatar = "https://static.productionready.io/images/smiley-cyrus.jpg";
  }

  @Test
  public void should_create_user_success() {

    String email = "1027@qq.com";
    String userName = "diego";
    User user = new User(email, userName, "123", "", defaultAvatar);
    UserData userData = new UserData(user.getId(), user.getEmail(), user.getUserName(),
                                     user.getBio(), user.getImage());

    when(userRepository.findByUserName(eq(userName))).thenReturn(Optional.empty());
    when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());
    when(userReadService.findById(any())).thenReturn(userData);
    when(jwtService.toToken(any())).thenReturn("123");

    Map<String, Object> param = prepareRegisterParameter(email, userName);

    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users")
        .prettyPeek()
        .then()
        .statusCode(201)
        .body("user.email", equalTo(email))
        .body("user.userName", equalTo(userName))
        .body("user.bio", equalTo(""))
        .body("user.image", equalTo(defaultAvatar))
        .body("user.token", equalTo("123"));
  }

  @Test
  public void should_login_success() {
    String email = "1027@qq.com";
    String username = "diego";
    String password = "123";

    User user = new User(email, username, password, "", defaultAvatar);
    UserData userData = new UserData("123", email, username, "", defaultAvatar);

    when(userRepository.findByEmail(eq(user.getEmail()))).thenReturn(Optional.ofNullable(user));
    when(userReadService.findById(eq(user.getId()))).thenReturn(userData);
    when(jwtService.toToken(any())).thenReturn("123");

    Map<String,Object> param = new HashMap<String,Object>(){{
      put("user",new HashMap<String,Object>(){{
        put("email",email);
        put("password",password);
      }});
    }};

    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users/login")
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("user.userName",equalTo(user.getUserName()))
        .body("user.email",equalTo(user.getEmail()))
        .body("user.bio",equalTo(""))
        .body("user.image",equalTo(defaultAvatar))
        .body("user.token",equalTo("123"));

  }

  private HashMap<String, Object> prepareRegisterParameter(final String email,
                                                           final String username) {
    return new HashMap<String, Object>() {{
      put("user", new HashMap<String, Object>() {{
        put("email", email);
        put("password", "johnnyjacob");
        put("userName", username);
      }});
    }};
  }
}