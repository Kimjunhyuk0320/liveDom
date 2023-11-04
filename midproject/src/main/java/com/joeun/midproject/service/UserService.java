package com.joeun.midproject.service;

import com.joeun.midproject.dto.Users;

public interface UserService {

  // 회원 조회
  public Users read(String username);

  // 회원 닉네임 조회
  public Users readOnlyNickname(String nickname);

  // 회원 연락처 조회
  public Users readOnlyPhone(String phone);

  public int insert(Users users);

  public int update(Users users);

}
