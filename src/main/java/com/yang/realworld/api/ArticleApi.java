package com.yang.realworld.api;


import com.yang.realworld.api.exception.ResourceNotFoundException;
import com.yang.realworld.application.ArticleQueryService;
import com.yang.realworld.application.data.ArticleData;
import com.yang.realworld.domain.article.ArticleRepository;
import com.yang.realworld.domain.user.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles/{slug}")
public class ArticleApi {

  private ArticleQueryService articleQueryService;
  private ArticleRepository articleRepository;

  @Autowired
  public ArticleApi(ArticleQueryService articleQueryService,
                    ArticleRepository articleRepository) {
    this.articleQueryService = articleQueryService;
    this.articleRepository = articleRepository;
  }

  @GetMapping
  public ResponseEntity<?> article(@PathVariable String slug, @AuthenticationPrincipal User user) {
    return articleQueryService.findBySlug(slug, user)
        .map(articleData -> ResponseEntity.ok(articleResponse(articleData))).orElseThrow(
            ResourceNotFoundException::new);
  }

  private Map<String, Object> articleResponse(ArticleData articleData) {
    return new HashMap<String, Object>() {{
      put("article", articleData);
    }};
  }
}
