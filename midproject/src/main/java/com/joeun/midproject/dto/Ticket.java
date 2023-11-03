package com.joeun.midproject.dto;

import lombok.Data;

@Data
public class Ticket {
    private int ticketNo;
    private String reservationNo;
    private int boardNo;
    private String name;
    private String phone;
    private String updDate;
    private int refund;
}
