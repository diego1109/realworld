package com.yang.realworld.infrastructure.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.yang.realworld.domain.user.FollowRelation;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
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
  private FollowRelation followRelation;

  private User user;
  private String anotherUserId = "haha";


  @Before
  public void setUp() throws Exception {
    user = new User("hdj@qq.com", "diego", "234", "", "default");
    followRelation  = new FollowRelation(user.getId(),anotherUserId);
  }

  @Test
  public void should_save_and_fetch_follow_relation_succeed() throws Exception{
    userRepository.saveRelation(followRelation);
    Optional<FollowRelation> optional = userRepository.findRelation(user.getId(),anotherUserId);

    assertThat(optional.isPresent(),is(true));
    FollowRelation refetch = optional.get();
    assertThat(refetch.getUserId(), is(user.getId()));
    assertThat(refetch.getTargetId(),is(anotherUserId));
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

  @Test
  public void should_unfollow_user_succeed(){
    User other = new User("other@example.com", "other", "123", "", "");
    userRepository.save(other);
    FollowRelation followRelation = new FollowRelation(user.getId(),other.getId());
    userRepository.saveRelation(followRelation);
    userRepository.removeRelation(followRelation);
    assertThat(userRepository.findRelation(user.getId(),other.getId()).isPresent(),is(false));
  }
}
