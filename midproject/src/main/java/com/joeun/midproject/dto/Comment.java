package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Comment {

  private int commentNo;
  private String parentTable;
  private int parentNo;
  private int sequence;
  private String writer;
  private String username;
  private String content;
  private Date regDate;
  private Date updDate;
  private int totalCount;
  private int profileNo;
  
}
