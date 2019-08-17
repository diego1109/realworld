package com.yang.realworld.infrastructure.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.yang.realworld.domain.Comment;
import com.yang.realworld.domain.CommentRepository;
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
@Import(MybatisCommentRepository.class)
public class MybatisCommentRepositoryTest {

  @Autowired
  private CommentRepository commentRepository;

  @Test
  public void should_save_comment_succeed() {
    Comment comment = new Comment("comment body", "diego", "article id");
    commentRepository.save(comment);
    Optional<Comment> optional = commentRepository
        .findById(comment.getArticleId(), comment.getId());
    assertThat(optional.isPresent(), is(true));
    Comment refetch = optional.get();
    assertThat(refetch.getId(), is(comment.getId()));
    assertThat(refetch.getArticleId(), is(comment.getArticleId()));
    assertThat(refetch.getBody(), is(comment.getBody()));
    assertThat(refetch.getUserId(), is(comment.getUserId()));
  }

}