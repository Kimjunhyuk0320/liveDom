package com.joeun.midproject.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;

public interface UserService {

  // 회원 조회
  public Users read(String username);

  // 회원 닉네임 조회
  public Users readOnlyNickname(String nickname);

  // 회원 연락처 조회
  public Users readOnlyPhone(String phone);

  public int insert(Users users,HttpServletRequest request) throws Exception;

  public int update(Users users,HttpServletRequest request,HttpServletResponse response)throws Exception;


  //유저 전화번호로 구매한 티켓 조회하기
  public List<Ticket> listByPhone(Users users) throws Exception;

  //게시글 작성자아이디로 판매한 티켓 목록 조회하기
  public List<Ticket> listByUserName(Users users) throws Exception;


}
