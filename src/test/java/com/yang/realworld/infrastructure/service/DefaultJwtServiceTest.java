package com.yang.realworld.infrastructure.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

import com.yang.realworld.domain.service.JwtService;
import com.yang.realworld.domain.user.User;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class DefaultJwtServiceTest {

  private JwtService jwtService;

  @Before
  public void setUp() {
    jwtService = new DefaultJwtService("123123", 3600);
  }

  @Test
  public void should_generate_and_parse_token() throws Exception {
    User user = new User("email@qq.com", "username", "123", "", "");
    String token = jwtService.toToken(user);
    assertThat(token,notNullValue());
    Optional<String> optional = jwtService.getSubFromToken(token);
    assertThat(optional.isPresent(),is(true));
    assertThat(optional.get(),is(user.getId()));
  }
}
