package com.yang.realworld.core.user;

import org.springframework.stereotype.Service;

@Service
public interface EncryptService {
  String encrypt(String password);
  Boolean check(String checkpassword,String realPassword);
}
