package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Team {

  private int teamNo;
  private String title;
  private String writer;
  private String username;
  private String content;
  private String location;
  private String address;
  private String liveDate;
  private String liveStTime;
  private String liveEndTime;
  private int price;
  private int capacity;
  private String account;
  private int views;
  private int confirmed;
  private Date updDate;


  //현재모집인원
  private int recStatus;

  //성사공연속성
  private String crew;
  private Date confirmedDate;
  
}
