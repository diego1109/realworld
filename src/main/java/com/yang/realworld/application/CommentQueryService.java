package com.yang.realworld.application;

import com.yang.realworld.application.data.CommentData;
import com.yang.realworld.domain.user.User;
import java.util.Map;
import java.util.Optional;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.stereotype.Service;

@Service
public class CommentQueryService {

  public Optional<CommentData> findById(String id, User user) {
      return null;
  }
}
