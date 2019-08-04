package com.yang.realworld.domain.user;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
  void save(User user);

  Optional<User> findById(String id);

  Optional<User> findByUserName(String userName);

  Optional<User> findByEmail(String email);

  void saveRelation(FollowRelation followRelation);

  Optional<FollowRelation> findRelation(String userId,String targetId);
}
