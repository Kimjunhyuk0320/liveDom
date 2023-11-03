package com.joeun.midproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.Users;
import com.joeun.midproject.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserMapper userMapper;

  // 회원가입
  @Override
  public int insert(Users users) {

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userMapper.insert(users);

    return result;
  }

  // 회원 정보 수정
  @Override
  public int update(Users users) {

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userMapper.update(users);

    return result;

  }


  
}
