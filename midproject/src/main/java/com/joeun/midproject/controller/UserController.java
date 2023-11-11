package com.joeun.midproject.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;
import com.joeun.midproject.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;




@Slf4j
@Controller
public class UserController {

  @Autowired
  private UserService userService;
    
  @GetMapping(value="/exception")
  public String exception() {
      return "exception";
  }
  
 

  @GetMapping(value="/denied")
  public String denied() {
      return "denied";
  }
  

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
  public String joinPro(Users users,HttpServletRequest request) throws Exception{

    int result = userService.insert(users,request);

      if(result>0){
        return "redirect:/";

      }  else{
        return "redirect:/join";
      }

  }
  @GetMapping(value="/myPage")
  public String myPage() {
      return "myPage/myPage";
  }
  




  // 회원 정보 수정
  @Secured({"ROLE_USER", "ROLE_BAND", "ROLE_CLUB"})
  @GetMapping(value="/update")
  public String update() {
      return "update";
  }
  
  @PostMapping(value="/update")
  public String updatePro(Users users,HttpServletRequest request, HttpServletResponse response) throws Exception{

    int result = userService.update(users,request,response);

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



  //  유저 전화번호로 구매한 티켓 리스트 조회하기
@ResponseBody
	@RequestMapping(value = "/listByPhone")
	public ResponseEntity<List<Ticket>> listByPhone(Users users) throws Exception {
			List<Ticket> ticketList = userService.listByPhone(users);
	    return new ResponseEntity<List<Ticket>>(ticketList, HttpStatus.OK);
	}

  //  유저 아이디로 판매한 티켓 리스트 조회하기
@ResponseBody
 @RequestMapping(value = "/listByUserName")
	public ResponseEntity<List<Ticket>> listByUserName(Users users) throws Exception {
			List<Ticket> ticketList = userService.listByUserName(users);
	    return new ResponseEntity<List<Ticket>>(ticketList, HttpStatus.OK);
	}


  @Secured({"ROLE_USER", "ROLE_BAND", "ROLE_CLUB"})
  @GetMapping(value="/myPage/ticket_purchase_list")
  public String ticketPurchase() {
      return "/myPage/ticket_purchase_list";
  }

  @Secured({"ROLE_USER", "ROLE_BAND", "ROLE_CLUB"})
  @GetMapping(value="/myPage/ticket_sales_list")
  public String ticketSales() {
      return "/myPage/ticket_sales_list";
  }

  @GetMapping(value="/myPage/myInfo")
  public String myInfo() {

      return "/myPage/myInfo";
  }

  
  

  
  

}
