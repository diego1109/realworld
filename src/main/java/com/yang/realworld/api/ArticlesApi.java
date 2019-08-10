package com.yang.realworld.api;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.yang.realworld.api.exception.InvalidRequestException;
import com.yang.realworld.application.ArticleQueryService;
import com.yang.realworld.application.Page;
import com.yang.realworld.domain.article.Article;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.user.User;
import java.util.HashMap;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
public class ArticlesApi {

  private ArticleRepository articleRepository;
  private ArticleQueryService articleQueryService;

  @Autowired
  public ArticlesApi(ArticleRepository articleRepository,
                     ArticleQueryService articleQueryService) {
    this.articleRepository = articleRepository;
    this.articleQueryService = articleQueryService;
  }

  @PostMapping
  public ResponseEntity createArticles(@Valid @RequestBody NewArticleParam newArticleParam,
                                       BindingResult bindingResult,
                                       @AuthenticationPrincipal User user) {
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException();
    }

    Article article = new Article(newArticleParam.getTitle(), newArticleParam.getDescription(),
                                  newArticleParam.getBody(), newArticleParam.getTagList(),
                                  user.getId());
    articleRepository.save(article);
    return ResponseEntity.status(HttpStatus.CREATED).body(new HashMap<String, Object>() {{
      put("article", articleQueryService.findById(article.getId(), user).get());
    }});
  }

  @GetMapping
  public ResponseEntity getArticles(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "limit", defaultValue = "20") int limit,
                                    @RequestParam(value = "tag", required = false) String tag,
                                    @RequestParam(value = "favorited", required = false) String favoritedBy,
                                    @RequestParam(value = "author",required = false)String author,
                                    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(articleQueryService.findRecentArticles(tag, author, favoritedBy, new Page(offset, limit), user));
  }
}

@Getter
@JsonRootName("article")
@NoArgsConstructor
class NewArticleParam {

  @NotBlank(message = "can't be empty")
  private String title;
  @NotBlank(message = "can't be empty")
  private String description;
  @NotBlank(message = "can't be empty")
  private String body;
  private String[] tagList;
}