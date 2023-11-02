package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TeamApp {

  private int appNo;
  private int teamNo;
  private String title;
  private String teamTitle;
  private String phone;
  private String username;
  private String bandName;
  private String content;
  private Date updDate;
  private int approvalStatus;
  private int depositStatus;

  private String members;
  
}
