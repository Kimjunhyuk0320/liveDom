package com.joeun.midproject.service;

import java.util.List;

import com.joeun.midproject.dto.FacilityRental;

// 대관서비스입니다.
public interface FacilityRentalService {
    // 게시글 목록
    public List<FacilityRental> list() throws Exception;
    // 게시글 조회
    public FacilityRental select(int FacilityRental) throws Exception;
    // 게시글 등록
    public int insert(FacilityRental facilityRental) throws Exception;
    // 게시글 수정
    public int update(FacilityRental facilityRental) throws Exception;
    // 게시글 삭제
    public int delete(int facilityRental) throws Exception;
}
