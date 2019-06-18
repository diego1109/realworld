package com.yang.realworld.application.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserData {
  private String id;
  private String email;
  private String userName;
  private String bio;
  private String image;
}
