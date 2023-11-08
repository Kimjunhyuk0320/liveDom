package com.joeun.midproject.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Users {
    public String username;
    private String password;
    private String userPwCheck;     // 비밀번호 확인
    private String name;
    private String nickname;
    private String auth;
    private String email;
    private String phone;
    private int enabled;            // 휴면여부
    private Date updDate;
    private int profileNo;


    //프로필사진
    private MultipartFile file;

}
