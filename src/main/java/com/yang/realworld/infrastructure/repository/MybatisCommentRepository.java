package com.yang.realworld.infrastructure.repository;

import com.yang.realworld.domain.Comment;
import com.yang.realworld.domain.CommentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public class MybatisCommentRepository implements CommentRepository {

  @Override
  public void save(Comment comment) {

  }
}
