package com.yang.realworld.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;

import com.yang.realworld.application.ProfileQueryService;
import com.yang.realworld.application.data.ProfileData;
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

  @Before
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  public void should_get_profile_succeed() {
    ProfileData profileData = new ProfileData("userId", "userName", "", "", true);
    Mockito.when(profileQueryService.findByUsername(eq(profileData.getUsername()), eq(user)))
        .thenReturn(Optional.of(profileData));
    given()
        .header("Authorization", "Token " + token)
        .when()
        .get("profiles/{username}", profileData.getUsername())
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("profile.username",equalTo(profileData.getUsername()));
  }
}