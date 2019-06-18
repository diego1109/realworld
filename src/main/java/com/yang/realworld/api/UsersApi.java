package com.yang.realworld.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.yang.realworld.api.exception.InvalidRequestException;
import com.yang.realworld.application.UserQueryService;
import com.yang.realworld.application.data.UserData;
import com.yang.realworld.application.data.UserWithToken;
import com.yang.realworld.core.service.JwtService;
import com.yang.realworld.core.user.EncryptService;
import com.yang.realworld.core.user.User;
import com.yang.realworld.core.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersApi {
  private UserRepository userRepository;
  private EncryptService encryptService;
  private UserQueryService userQueryService;
  private JwtService jwtService;

  @Autowired
  public UsersApi(UserRepository userRepository, EncryptService encryptService,
                  UserQueryService userQueryService) {
    this.userRepository = userRepository;
    this.encryptService = encryptService;
    this.userQueryService = userQueryService;
  }

  @PostMapping()
  public ResponseEntity createUser(RegisterParam registerParam, BindingResult bindingResult) {
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
  };

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
      throw new InvalidRequestException(bindingResult);
    }
  }

  private Map<String,Object> userResponse(UserWithToken userWithToken){
    return new HashMap<String ,Object>(){{
      put("user",userWithToken);
    }};
  }
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class RegisterParam {
  @NotBlank(message = "can not be empty")
  private String userName;

  @NotBlank(message = "can not be empty")
  private String password;

  @NotBlank(message = "can not be empty")
  @Email(message = "should be email")
  private String email;
}
