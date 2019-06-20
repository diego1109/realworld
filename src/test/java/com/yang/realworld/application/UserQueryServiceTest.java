package com.yang.realworld.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.yang.realworld.application.data.UserData;
import com.yang.realworld.core.user.User;
import com.yang.realworld.core.user.UserRepository;
import com.yang.realworld.infrastructure.repository.MybatisUserRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = NONE)
@Import({MybatisUserRepository.class, UserQueryService.class})
public class UserQueryServiceTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserQueryService userQueryService;
  private User user;

  @Before
  public void setUp() {
    user = new User("hdj@qq.com", "diego", "234", "", "default");
  }

  @Test
  public void should_query_by_user_id_success() {
    userRepository.save(user);
    Optional<UserData> optionalUserData = userQueryService.findById(user.getId());
    assertThat(optionalUserData.get().getUserName(), is(user.getUserName()));
    assertThat(optionalUserData.get().getBio(), is(user.getBio()));
    assertThat(optionalUserData.get().getEmail(), is(user.getEmail()));
    assertThat(optionalUserData.get().getImage(), is(user.getImage()));
  }

}