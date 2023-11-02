package com.joeun.midproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.Users;
import com.joeun.midproject.mapper.UserTempMapper;

@Service
public class UserTempServiceImpl implements UserTempService{

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserTempMapper userTempMapper;

  @Override
  public int insert(Users users) {

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userTempMapper.insert(users);

    return result;
  }


  
}
