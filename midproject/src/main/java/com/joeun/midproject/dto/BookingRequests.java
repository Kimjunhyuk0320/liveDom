package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BookingRequests {
    private int brNo;
    private int frNo;
    private String username;
    private String name;
    private String writer;
    private String phone;
    private Date updDate;
    private int approvalStatus;
    private int depositStatus;
    private String frTitle;
    private String frDate;
    private String frPhone;
    private String frAddress;
    private String frAccount;
    private String frName;


    
}
