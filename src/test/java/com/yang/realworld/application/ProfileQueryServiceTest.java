package com.yang.realworld.application;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import com.yang.realworld.infrastructure.repository.MyBatisUserRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({MyBatisUserRepository.class, ProfileQueryService.class})
public class ProfileQueryServiceTest {

  @Autowired
  private ProfileQueryService profileQueryService;
  @Autowired
  private UserRepository userRepository;

  @Test
  public void should_fetch_profile_succeed() {
    User currentUser = new User("a@test.com", "a", "123", "", "");
    User profileUser = new User("p@test.com", "p", "123", "", "");
    userRepository.save(profileUser);

    Optional<ProfileData> optional = profileQueryService
        .findByUsername(profileUser.getUserName(), currentUser);
    assertThat(optional.isPresent(), is(true));
    ProfileData profileData = optional.get();
    assertThat(profileData.getId(), is(profileUser.getId()));
    assertThat(profileData.getUsername(), is(profileUser.getUserName()));
    assertThat(profileData.getBio(), is(profileUser.getBio()));
  }
}