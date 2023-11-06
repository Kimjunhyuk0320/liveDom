package com.joeun.midproject.service;

import java.util.List;

import com.joeun.midproject.dto.BookingRequests;
import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.Team;


public interface FacilityRentalService {
    // 게시글 목록
    public List<FacilityRental> list() throws Exception;

    //페이지네이션 게시글 조회
    public List<FacilityRental> pageFrList(Team team) throws Exception;

    // 받은 대관 신청 조회
    public List<BookingRequests> rrList(String username) throws Exception;

    // 내가 신청한 예약 조회
    public List<BookingRequests> rreqList(String username) throws Exception;

    // 게시글 조회
    public FacilityRental select(int FacilityRental) throws Exception;
    // 게시글 등록
    public int insert(FacilityRental facilityRental) throws Exception;
    // 게시글 수정
    public int update(FacilityRental facilityRental) throws Exception;
    // 게시글 삭제
    public int delete(int facilityRental) throws Exception;

    // 예약 신청 삭제
    public int delReq(BookingRequests bookingRequests) throws Exception;

    // 대관 예약 신청
    public int reservation(BookingRequests bookingRequests) throws Exception;

    //대관 신청 거절
    public int reqDenied(BookingRequests bookingRequests) throws Exception;

     //예약 신청 승인
    public int reqAccept(BookingRequests bookingRequests) throws Exception;

     //예약 신청 확정
    public int reqConfirm(BookingRequests bookingRequests) throws Exception;
}
