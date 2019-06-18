package com.yang.realworld.infrastructure.service;

import com.yang.realworld.core.user.EncryptService;

public class NaiveEncryptService implements EncryptService {

  @Override
  public String encrypt(String password) {
    return password;
  }

  @Override
  public Boolean check(String checkpassword, String realPassword) {
    return checkpassword.equals(realPassword);
  }
}
