package com.joeun.midproject.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class LiveBoard {
    private int boardNo;
    private String title;
    private String writer;
    private String username;
    private String content;
    private String crew;
    private int price;
    private String liveDate;
    private String liveTime;
    private String liveStTime;
    private String liveEndTime;
    private String location;
    private String address;
    private int maxTickets;
    private String updDate;
    private int soldOut;
    private int ticketLeft;
    private Integer fileNo;
    private Files thumbnail;
    private List<MultipartFile> file;



}
