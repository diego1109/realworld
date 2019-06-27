package com.yang.realworld.infrastructure.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.yang.realworld.core.user.User;
import com.yang.realworld.core.user.UserRepository;
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
@Import(MyBatisUserRepository.class)
@AutoConfigureTestDatabase(replace = NONE)
public class MybatisUserRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User("hdj@qq.com", "diego", "234", "", "default");
  }

  @Test
  public void should_save_and_fetch_user_success() throws Exception{
    userRepository.save(user);
    Optional<User> userOptional1 = userRepository.findById(user.getId());
    assertThat(userOptional1.get().getEmail(),is(user.getEmail()));

    Optional<User> userOptional2 = userRepository.findByEmail("hdj@qq.com");
    assertThat(userOptional2.get().getUserName(),is(user.getUserName()));

    Optional<User> userOptional3 = userRepository.findByUserName("diego");
    assertThat(userOptional3.get().getId(),is(user.getId()));
  }

  @Test
  public void shuould_update_user_success() {
    userRepository.save(user);
    String newEmail = "2248@qq.com";
    user.update(newEmail, "", "", "", "");
    userRepository.save(user);
    Optional<User> optionalUser = userRepository.findById(user.getId());
    assertThat(optionalUser.get().getEmail(), is(newEmail));

    String newName = "newUserName";
    user.update("", newName, "", "", "");
    userRepository.save(user);
    Optional<User> optionalUser2 = userRepository.findById(user.getId());
    assertThat(optionalUser2.get().getUserName(), is(newName));

  }
}
