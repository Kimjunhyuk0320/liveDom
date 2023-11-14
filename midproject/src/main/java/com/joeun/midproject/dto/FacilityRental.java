package com.joeun.midproject.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FacilityRental {
    private int frNo;
    private String title;
    private String writer;
    private String username;
    private String content;
    private int price;
    private String location;
    private String address;
    private String liveDate;
    private Date updDate;
    private String phone;
    private String account;
    private String account1;
    private String account2;
    private int views;
    private String buyUsername;
    private int confirmed;

    private List<MultipartFile> file;
    private Files thumbnail;
}
