package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.BookingRequests;

@Mapper
public interface BookingRequestsMapper {
    // 유저 예약 목록
    public List<BookingRequests> reservationList() throws Exception;

    // 클럽 대관 목록
    public List<BookingRequests> rentalList() throws Exception;

    // 예약신청
    public int reservation(BookingRequests bookingRequests) throws Exception;
}
