package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.BookingRequests;

@Mapper
public interface BookingRequestsMapper {
    // 유저 예약 목록
    public List<BookingRequests> rreqList(String username) throws Exception;

    // 대관 신청 목록 
    public BookingRequests listBybrNo(int brNo) throws Exception;

    // 클럽 대관 목록
    public List<BookingRequests> rentalList(String username) throws Exception;

    // 예약신청
    public int reservation(BookingRequests bookingRequests) throws Exception;

    //예약 신청 삭제
    public int delReq(BookingRequests bookingRequests) throws Exception;

    //예약 신청 거절
    public int reqDenied(BookingRequests bookingRequests) throws Exception;

    //확정 시 나머지 예약 거절
    public int reqDeniedAll(BookingRequests bookingRequests) throws Exception;

    //예약 신청 승인
    public int reqAccept(BookingRequests bookingRequests) throws Exception;

    //예약 신청 확정
    public int reqConfirm(BookingRequests bookingRequests) throws Exception;

    //예약 확정 시 대관에 확정자 닉네임 등록   
    public int confirmUsername(BookingRequests bookingRequests) throws Exception;
    
}
