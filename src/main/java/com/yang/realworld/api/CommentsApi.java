package com.yang.realworld.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.yang.realworld.api.exception.InvalidRequestException;
import com.yang.realworld.api.exception.NoAuthorizationException;
import com.yang.realworld.api.exception.ResourceNotFoundException;
import com.yang.realworld.application.CommentQueryService;
import com.yang.realworld.application.data.CommentData;
import com.yang.realworld.domain.Comment;
import com.yang.realworld.domain.CommentRepository;
import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.service.AuthorizationService;
import com.yang.realworld.domain.user.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "articles/{slug}/comments")
public class CommentsApi {

  private ArticleRepository articleRepository;
  private CommentRepository commentRepository;
  private CommentQueryService commentQueryService;

  @Autowired
  public CommentsApi(ArticleRepository articleRepository,
                     CommentRepository commentRepository,
                     CommentQueryService commentQueryService) {
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
    this.commentQueryService = commentQueryService;
  }

  @PostMapping
  public ResponseEntity createComment(@PathVariable("slug") String slug,
                                      @Valid @RequestBody NewCommentParam newCommentParam,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal User user) {
    Article article = articleRepository.findBySlug(slug)
        .orElseThrow(ResourceNotFoundException::new);
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException();
    }
    Comment comment = new Comment(newCommentParam.getBody(), user.getId(), article.getId());
    commentRepository.save(comment);
    return ResponseEntity.status(201)
        .body(commentResponse(commentQueryService.findById(comment.getId(), user).get()));
  }

  @GetMapping
  public ResponseEntity getComments(@PathVariable("slug") String slug,
                                    @AuthenticationPrincipal User user) {

    Article article = articleRepository.findBySlug(slug).orElseThrow(NoResultException::new);
    List<CommentData> commentDatas = commentQueryService.findByArticleId(article.getId(), user);
    return ResponseEntity.ok(new HashMap<String, Object>() {{put("comment", commentDatas);}});
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteComment(@PathVariable("slug") String slug,
                                      @PathVariable("id") String id,
                                      @AuthenticationPrincipal User user) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(NoResultException::new);
    Comment comment = commentRepository.findById(article.getId(), id)
        .orElseThrow(NoResultException::new);
    if (!AuthorizationService.canWriteComment(user, article, comment)) {
      throw new NoAuthorizationException();
    }
    commentRepository.remove(comment);
    return ResponseEntity.noContent().build();
  }

  private Map<String, Object> commentResponse(CommentData commentData) {
    return new HashMap<String, Object>() {{
      put("comment", commentData);
    }};
  }
}

@Getter
@JsonRootName("comment")
class NewCommentParam {

  @NotNull
  private String body;
}