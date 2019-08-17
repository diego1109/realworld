package com.yang.realworld.domain;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface CommentRepository {

  void save(Comment comment);

  Optional<Comment> findById(String articleId, String id);
}
