package com.yang.realworld.application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.yang.realworld.application.data.CommentData;
import com.yang.realworld.domain.Comment;
import com.yang.realworld.domain.CommentRepository;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.user.FollowRelation;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import com.yang.realworld.infrastructure.repository.MyBatisArticleRepository;
import com.yang.realworld.infrastructure.repository.MyBatisUserRepository;
import com.yang.realworld.infrastructure.repository.MybatisCommentRepository;
import java.util.Optional;
import org.junit.Before;
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
@Import({CommentQueryService.class,
    MyBatisArticleRepository.class,
    MybatisCommentRepository.class,
    MyBatisUserRepository.class})
public class CommentQueryServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private CommentQueryService queryService;

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User("1027@qq.com", "diego", "123", "", "");
    userRepository.save(user);
  }

  @Test
  public void should_query_comment_by_id_succeed() {
    User commentdUser = new User("1234@qq.com", "mason", "123", "", "");
    userRepository.save(commentdUser);
    Comment comment = new Comment("comment body", commentdUser.getId(), "article Id");
    commentRepository.save(comment);
    userRepository.saveRelation(new FollowRelation(user.getId(), commentdUser.getId()));
    
    Optional<CommentData> optional = queryService.findById(comment.getId(), user);
    assertThat(optional.isPresent(), is(true));
    CommentData refetch = optional.get();
    assertThat(refetch.getId(), is(comment.getId()));
    assertThat(refetch.getProfileData().getId(), is(commentdUser.getId()));
    assertThat(refetch.getProfileData().isFollowing(), is(true));
  }
}