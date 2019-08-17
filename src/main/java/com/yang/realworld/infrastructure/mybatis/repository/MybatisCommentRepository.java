package com.yang.realworld.infrastructure.mybatis.repository;

import com.yang.realworld.domain.Comment;
import com.yang.realworld.domain.CommentRepository;
import com.yang.realworld.infrastructure.mybatis.mapper.CommentMapper;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisCommentRepository implements CommentRepository {

  private CommentMapper commentMapper;

  @Autowired
  public MybatisCommentRepository(
      CommentMapper commentMapper) {
    this.commentMapper = commentMapper;
  }

  @Override
  public void save(Comment comment) {
    commentMapper.insert(comment);
  }

  @Override
  public Optional<Comment> findById(String articleId, String id) {
    return Optional.ofNullable(commentMapper.findById(articleId, id));
  }


}
