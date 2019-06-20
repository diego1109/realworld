package com.yang.realworld.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserData {
  private String id;
  private String email;
  private String userName;
  private String bio;
  private String image;
}
