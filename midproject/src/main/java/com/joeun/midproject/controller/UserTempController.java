package com.joeun.midproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joeun.midproject.dto.Users;
import com.joeun.midproject.service.UserTempService;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@Controller
public class UserTempController {

  @Autowired
  private UserTempService userTempService;

  @GetMapping(value="/login")
  public String login() {
      return "login";
  }
  

  @GetMapping(value="/join")
  public String join() {
      return "join";
  }
  
  

  @PostMapping(value="/join")
  public String joinPro(Users users) {

    int result = userTempService.insert(users);

      
      return "redirect:/team";
  }
  
}
