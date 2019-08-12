package com.yang.realworld.infrastructure.mybatis;

import com.yang.realworld.application.Page;
import com.yang.realworld.application.data.ArticleData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ArticleReadService {

  List<ArticleData> findArticlesOfAuthors(@Param("followedUsers") List<String> followedUsers, @Param("page") Page page);

  ArticleData findBySlug(@Param("slug") String slug);

  List<String> queryArticles(@Param("tag") String tag, @Param("author") String author,
                             @Param("favoritedBy") String favoritedBy, @Param("page") Page page);

  int countArtile(@Param("tag") String tag, @Param("author") String author, @Param("favoritedBy") String favoritedBy);

  List<ArticleData> findArticles(@Param("articleIds") List<String> articleIds);

  int countFeedSize(@Param("authors") List<String> followedUsers);
}
