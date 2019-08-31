package com.yang.realworld.api;

import com.yang.realworld.application.TagsQueryService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsApi {

  private TagsQueryService tagsQueryService;

  @Autowired
  public TagsApi(TagsQueryService tagsQueryService) {
    this.tagsQueryService = tagsQueryService;
  }

  @GetMapping
  public ResponseEntity getTags() {
    List<String> tags = tagsQueryService.allTags();
    return ResponseEntity.ok(new HashMap<String, Object>() {{put("tags", tags);}});
  }

}
