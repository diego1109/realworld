package com.yang.realworld.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.yang.realworld.api.exception.InvalidRequestException;
import com.yang.realworld.application.UserQueryService;
import com.yang.realworld.application.data.UserData;
import com.yang.realworld.application.data.UserWithToken;
import com.yang.realworld.domain.service.JwtService;
import com.yang.realworld.domain.user.EncryptService;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersApi {
  private UserRepository userRepository;
  private EncryptService encryptService;
  private UserQueryService userQueryService;
  private JwtService jwtService;
  private String defaultImage;

  @Autowired
  public UsersApi(
      UserRepository userRepository,
      EncryptService encryptService,
      UserQueryService userQueryService,
      JwtService jwtService,
      @Value("image.default") String defaultImage) {
    this.userRepository = userRepository;
    this.encryptService = encryptService;
    this.userQueryService = userQueryService;
    this.jwtService = jwtService;
    this.defaultImage = defaultImage;
  }

  @PostMapping
  public ResponseEntity createUser(
      @Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
    checkInput(registerParam, bindingResult);
    User user =
        new User(
            registerParam.getEmail(),
            registerParam.getUserName(),
            encryptService.encrypt(registerParam.getPassword()),
            "",
            "defaultImage");

    userRepository.save(user);
    UserData userData = userQueryService.findById(user.getId()).get();

    return ResponseEntity.status(201)
        .body(userResponse(new UserWithToken(userData, jwtService.toToken(user))));
  }

  private void checkInput(RegisterParam registerParam, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException(bindingResult);
    }
    if (userRepository.findByUserName(registerParam.getUserName()).isPresent()) {

      bindingResult.rejectValue("userName", "DUPLICATED", "duplicated userName");
    }
    if (userRepository.findByEmail(registerParam.getEmail()).isPresent()) {
      bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
    }
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException();
    }
  }

  @PostMapping("/login")
  public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam,
                                  BindingResult bindingResult) {
    Optional<User> optional = userRepository.findByEmail(loginParam.getEmail());
    if (optional.isPresent() && encryptService
        .check(loginParam.getPassword(), optional.get().getPassword())) {
      UserData userData = userQueryService.findById(optional.get().getId()).get();
      return ResponseEntity
          .ok(userResponse(new UserWithToken(userData, jwtService.toToken(optional.get()))));
    } else {
      bindingResult.rejectValue("password", "INVALID", "invalid email or password");
      throw new InvalidRequestException(bindingResult);
    }
  }

  private Map<String, Object> userResponse(UserWithToken userWithToken) {
    return new HashMap<String, Object>() {
      {
        put("user", userWithToken);
      }
    };
  }


}


@Getter
@JsonRootName("user")
@NoArgsConstructor
class LoginParam {

  @NotBlank(message = "can't be empty")
  @Email(message = "should be email")
  private String email;
  @NotBlank(message = "can't be empty")
  private String password;
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
@ToString
class RegisterParam {
  @NotBlank(message = "can not be empty")
  private String userName;

  @NotBlank(message = "can not be empty")
  private String password;

  @NotBlank(message = "can not be empty")
  @Email(message = "should be email")
  private String email;
}
