package com.yang.realworld;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.yang.realworld.api.exception.InvalidRequestException;
import com.yang.realworld.application.ArticleQueryService;
import com.yang.realworld.core.article.Article;
import com.yang.realworld.core.article.ArticleRepository;
import com.yang.realworld.core.user.User;
import java.util.HashMap;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    return ResponseEntity.ok(new HashMap<String, Object>() {{
      put("article", articleQueryService.findById(article.getId(), user).get());
    }});
  }

}

@Getter
@JsonRootName("acticle")
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