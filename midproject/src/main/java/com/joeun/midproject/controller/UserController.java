package com.joeun.midproject.controller;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joeun.midproject.dto.Users;
import com.joeun.midproject.service.UserService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 로그인 화면
   * @return
   */
  @GetMapping(value="/login")
  public String login(@CookieValue(value = "remember-id", required = false) Cookie cookie, Model model ) {
    String userId = "";
    boolean rememberId = false;
    if( cookie != null ) {
      log.info("CookieName : " + cookie.getName() );
      log.info("CookieValue : " +  cookie.getValue() );
      userId = cookie.getValue();
      rememberId = true;
    }

    model.addAttribute("username", userId);
    model.addAttribute("rememberId", rememberId);

    return "login";
  }
  
  // 회원가입
  @GetMapping(value="/join")
  public String join() {
      return "join";
  }
  
  @PostMapping(value="/join")
  public String joinPro(Users users) {

    int result = userService.insert(users);

      if(result>0){
        return "redirect:/";

      }  else{
        return "redirect:/join";
      }

  }
  
  // 회원 정보 수정
  @GetMapping(value="/update")
  public String update() {
      return "update";
  }
  
  @PostMapping(value="/update")
  public String updatePro(Users users) {

    int result = userService.update(users);

      if(result>0){
        return "redirect:/";

      }  else{
        return "redirect:/update";
      }

  }

  // 로그인 중복 검사
  @ResponseBody 
  @GetMapping(value = "/getLoginIdDup")
  public String getLoginIdDup(String username) {

    log.info("test1 : " + username);
    Users user = userService.read(username);
    log.info("test2 : " + user);

    if( user != null ) {
      return "N";
    } 
    else {
      return "Y";
    }
 
  }

  // 닉네임 중복 검사 getPhoneDup
  @ResponseBody 
  @GetMapping(value = "/getNicknameDup")
  public String getNicknameDup(String nickname) {

    Users user = userService.readOnlyNickname(nickname);

    if( user != null ) {
      return "N";
    } 
    else {
      return "Y";
    }
 
  }

  // 연락처 중복 검사
  @ResponseBody 
  @GetMapping(value = "/getPhoneDup")
  public String getPhoneDup(String phone) {

    Users user = userService.readOnlyPhone(phone);

    if( user != null ) {
      return "N";
    } 
    else {
      return "Y";
    }
 
  }
}
