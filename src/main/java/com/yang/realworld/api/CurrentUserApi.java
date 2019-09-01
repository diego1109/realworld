package com.yang.realworld.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.yang.realworld.api.exception.InvalidRequestException;
import com.yang.realworld.application.UserQueryService;
import com.yang.realworld.application.data.UserData;
import com.yang.realworld.application.data.UserWithToken;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class CurrentUserApi {

  private UserQueryService userQueryService;
  private UserRepository userRepository;

  @Autowired
  public CurrentUserApi(UserQueryService userQueryService,
                        UserRepository userRepository) {
    this.userQueryService = userQueryService;
    this.userRepository = userRepository;
  }

  @GetMapping
  public ResponseEntity currentUser(@AuthenticationPrincipal User current,
                                    @RequestHeader(value = "Authorization") String authorization) {
    UserData userData = userQueryService.findById(current.getId()).get();
    return ResponseEntity
        .ok(userResponse(new UserWithToken(userData, authorization.split(" ")[1])));
  }

  @PutMapping
  public ResponseEntity updateProfile(@AuthenticationPrincipal User current,
                                      @RequestHeader(value = "Authorization") String token,
                                      UpdateUserParam updateUserParam,
                                      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException();
    }
    checkUniquenessOfUsernameAndEmail(current, updateUserParam, bindingResult);
    current.update(updateUserParam.getEmail(),
        updateUserParam.getUsername(),
        updateUserParam.getPassowrd(),
        updateUserParam.getBio(),
        updateUserParam.getImage());
    userRepository.save(current);
    UserData userData = userQueryService.findById(current.getId()).get();
    return ResponseEntity
        .ok(userResponse(new UserWithToken(userData, token.split(" ")[1])));
  }

  private void checkUniquenessOfUsernameAndEmail(User current, UpdateUserParam updateUserParam,
                                                 BindingResult bindingResult) {
    if (updateUserParam.getUsername() != null) {
      Optional<User> byUsername = userRepository.findByUsername(updateUserParam.getUsername());
      if (byUsername.isPresent() && byUsername.get().equals(current)) {
        bindingResult.rejectValue("username", "DUPLICATED", "username is already exist");
      }
    }

    if (updateUserParam.getEmail() != null) {
      Optional<User> byUserEmail = userRepository.findByEmail(updateUserParam.getEmail());
      if (byUserEmail.isPresent() && byUserEmail.get().equals(current)) {
        bindingResult.rejectValue("email", "DUPLICATED", "email is already exist");
      }
    }

    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException();
    }

  }

  private Map<String, Object> userResponse(UserWithToken userWithToken) {
    return new HashMap<String, Object>() {{put("user", userWithToken);}};
  }
}


@Getter
@JsonRootName("user")
@NoArgsConstructor
class UpdateUserParam {

  @Email(message = "should be email")
  private String email;
  private String passowrd;
  private String username;
  private String bio;
  private String image;
}