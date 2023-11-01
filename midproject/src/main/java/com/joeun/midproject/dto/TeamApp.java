package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TeamApp {

  private int appNo;
  private int teamNo;
  private String title;
  private String phone;
  private String username;
  private String band_name;
  private String content;
  private Date upd_date;
  private int approvalStatus;
  private int depositStatus;
  
}
