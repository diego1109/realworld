package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yang.realworld.application.ProfileQueryService;
import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.domain.user.FollowRelation;
import com.yang.realworld.domain.user.User;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProfileApi.class)
public class ProfileApiTest extends TestWithCurrentUser {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileQueryService profileQueryService;

  private ProfileData profileData;
  private User anotherUser;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mockMvc);
    anotherUser = new User("username@test.com", "username", "123", "", "");
    profileData = new ProfileData(anotherUser.getId(), anotherUser.getUserName(),
        anotherUser.getBio(), anotherUser.getImage(), false);
  }

  @Test
  public void should_get_profile_succeed() {

    Mockito.when(profileQueryService.findByUsername(eq(profileData.getUsername()), eq(user)))
        .thenReturn(Optional.of(profileData));
    given()
        .header("Authorization", "Token " + token)
        .when()
        .get("/profiles/{username}", profileData.getUsername())
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("profile.username",equalTo(profileData.getUsername()));
  }

  @Test
  public void should_follow_user_succeed() {
    Mockito.when(userRepository.findByUserName(eq(anotherUser.getUserName())))
        .thenReturn(Optional.of(anotherUser));

    Mockito.when(profileQueryService.findByUsername(eq(anotherUser.getUserName()), eq(user)))
        .thenReturn(Optional.of(profileData));
    given()
        .header("Authorization", "Token " + token)
        .when()
        .post("/profiles/{username}/follow", anotherUser.getUserName())
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("profile.username", equalTo(profileData.getUsername()));
  }

  @Test
  public void should_unfollow_user_success() throws Exception {
    Mockito.when(userRepository.findByUserName(eq(anotherUser.getUserName())))
        .thenReturn(Optional.of(anotherUser));
    FollowRelation followRelation = new FollowRelation(user.getId(), anotherUser.getId());
    when(userRepository.findRelation(eq(user.getId()), eq(anotherUser.getId()))).thenReturn(Optional.of(followRelation));
    when(profileQueryService.findByUsername(eq(profileData.getUsername()), eq(user))).thenReturn(Optional.of(profileData));

    given()
        .header("Authorization", "Token " + token)
        .when()
        .delete("/profiles/{username}/follow", anotherUser.getUserName())
        .prettyPeek()
        .then()
        .statusCode(200);

    verify(userRepository).removeRelation(eq(followRelation));
  }
}