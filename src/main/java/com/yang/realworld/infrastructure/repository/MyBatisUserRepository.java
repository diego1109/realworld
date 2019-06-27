package com.yang.realworld.infrastructure.repository;

import com.yang.realworld.core.user.User;
import com.yang.realworld.core.user.UserRepository;
import com.yang.realworld.infrastructure.mybatis.mapper.UserMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisUserRepository implements UserRepository {

  private UserMapper userMapper;

  @Autowired
  public MyBatisUserRepository(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public void save(User user) {
    if(userMapper.findById(user.getId()) == null){
      userMapper.insert(user);
    }else {
      userMapper.update(user);
    }
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(userMapper.findById(id));
  }

  @Override
  public Optional<User> findByUserName(String userName) {
    return Optional.ofNullable(userMapper.findByName(userName));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(userMapper.findByEmail(email));
  }
}
