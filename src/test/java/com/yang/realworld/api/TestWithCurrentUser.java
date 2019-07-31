package com.yang.realworld.api;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.yang.realworld.application.data.UserData;
import com.yang.realworld.domain.service.JwtService;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import com.yang.realworld.infrastructure.mybatis.readservice.UserReadService;
import java.util.Optional;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
abstract class TestWithCurrentUser {

  @MockBean
  protected UserRepository userRepository;

  @MockBean
  protected UserReadService userReadService;

  @MockBean
  protected JwtService jwtService;

  protected User user;
  protected UserData userData;
  protected String token;
  protected String email;
  protected String userName;
  protected String defaultAvatar;

  protected void userFixture() {
    email = "1027@qq.com";
    userName = "diego";
    defaultAvatar = "https://static.productionready.io/images/smiley-cyrus.jpg";

    user = new User(email, userName, "123", "", defaultAvatar);
    when(userRepository.findByUserName(eq(user.getUserName())))
        .thenReturn(Optional.of(user));
    when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));

    userData = new UserData(user.getId(),email,userName,"",defaultAvatar);
    when(userReadService.findById(eq(user.getId()))).thenReturn(userData);

    token = "token";
    when(jwtService.getSubFromToken(eq(token))).thenReturn(Optional.of(user.getId()));
  }

  @Before
  public void setUp() throws Exception{
    userFixture();
  }
}
