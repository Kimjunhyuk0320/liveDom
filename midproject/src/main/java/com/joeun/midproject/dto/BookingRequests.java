package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BookingRequests {
    private int brNo;
    private int frNo;
    private String username;
    private String writer;
    private String phone;
    private Date updDate;
    private int approvalStatus;
    private int depositStatus;

    
}
