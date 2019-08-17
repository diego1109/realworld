package com.yang.realworld.application;

import com.yang.realworld.application.data.CommentData;
import com.yang.realworld.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CommentQueryService {

  public Optional<CommentData> findById(String id, User user) {
      return null;
  }

  public List<CommentData> findByArticleId(String id, User user) {
    return null;
  }
}
