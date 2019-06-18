package com.yang.realworld.core.user;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
  void save(User user);

  Optional<User> findById(String id);

  Optional<User> findByUserName(String userName);

  Optional<User> findByEmail(String email);
}
