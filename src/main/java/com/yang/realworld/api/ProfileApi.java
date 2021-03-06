package com.yang.realworld.api;


import com.yang.realworld.api.exception.ResourceNotFoundException;
import com.yang.realworld.application.ProfileQueryService;
import com.yang.realworld.application.data.ProfileData;
import com.yang.realworld.domain.user.FollowRelation;
import com.yang.realworld.domain.user.User;
import com.yang.realworld.domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/profiles/{username}")
public class ProfileApi {

  private ProfileQueryService profileQueryService;
  private UserRepository userRepository;

  @Autowired
  public ProfileApi(ProfileQueryService profileQueryService,
                    UserRepository userRepository) {
    this.profileQueryService = profileQueryService;
    this.userRepository = userRepository;
  }

  @GetMapping
  public ResponseEntity getProfile(@PathVariable("username") String userName,
                                   @AuthenticationPrincipal User current) {
    ProfileData profileData = profileQueryService.findByUsername(userName, current).orElseThrow(
        ResourceNotFoundException::new);
    return ResponseEntity.ok(profileResponse(profileData));
  }

  @PostMapping("follow")
  public ResponseEntity follow(@PathVariable("username") String userName,
                               @AuthenticationPrincipal User current) {
    User user = userRepository.findByUserName(userName).orElseThrow(ResourceNotFoundException::new);
    userRepository.saveRelation(new FollowRelation(current.getId(), user.getId()));
    return ResponseEntity
        .ok(profileResponse(profileQueryService.findByUsername(user.getUserName(), current).get()));
  }

  @DeleteMapping("follow")
  public ResponseEntity unfollow(@PathVariable("username") String userName,
                                 @AuthenticationPrincipal User current) {
    User user = userRepository.findByUserName(userName).orElseThrow(ResourceNotFoundException::new);
    userRepository.removeRelation(userRepository.findRelation(current.getId(), user.getId()).get());
    return ResponseEntity
        .ok(profileResponse(profileQueryService.findByUsername(user.getUserName(), current).get()));
  }

  private Map<String, Object> profileResponse(ProfileData profileData) {
    return new HashMap<String, Object>() {{put("profile", profileData);}};
  }


}
