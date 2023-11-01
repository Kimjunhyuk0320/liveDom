package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.FacilityRental;

@Mapper
public interface FacilityRentalMapper {

    // 게시글 목록
    public List<FacilityRental> list() throws Exception;
    // 게시글 조회
    public FacilityRental select(int frNo) throws Exception;
    // 게시글 등록
    public int insert(FacilityRental facilityRental) throws Exception;
    // 게시글 수정
    public int update(FacilityRental facilityRental) throws Exception;
    // 게시글 삭제
    public int delete(int frNo) throws Exception;

    // 게시글 번호(기본키) 최대값
    public int maxPk() throws Exception;
}
