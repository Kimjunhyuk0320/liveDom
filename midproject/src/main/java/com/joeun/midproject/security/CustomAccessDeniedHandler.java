package com.joeun.midproject.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.joeun.midproject.dto.CustomUser;
import com.joeun.midproject.dto.Users;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

  @Override
  public void handle(HttpServletRequest request, 
                   HttpServletResponse response,
                   AccessDeniedException accessDeniedException) throws IOException, ServletException {


      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
        //로그인이지만 권한이 없을 시

            // CustomUser customUser = (CustomUser) authentication.getPrincipal();

            // Users user = customUser.getUsers();

            // String auth = user.getAuth();

            response.sendRedirect("/denied");

            

            

        } else{
          //비로그인시
          response.sendRedirect("/login");

        }
        
        




  }
  
}
