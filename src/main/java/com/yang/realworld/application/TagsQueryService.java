package com.yang.realworld.application;

import com.yang.realworld.infrastructure.mybatis.readservice.TagReadService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagsQueryService {

  private TagReadService tagReadService;

  @Autowired
  public TagsQueryService(TagReadService tagReadService) {
    this.tagReadService = tagReadService;
  }

  public List<String> allTags() {
    return tagReadService.all();
  }

}
