package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.Team;

@Mapper
public interface FacilityRentalMapper {

    // 게시글 목록
    public List<FacilityRental> list() throws Exception;

    //페이지네이션 게시글 조회
    public List<FacilityRental> pageFrList(Team team) throws Exception;
    // 게시글 조회
    public FacilityRental select(int frNo) throws Exception;

    public int viewsUp(int frNo) throws Exception;

    // 게시글 등록
    public int insert(FacilityRental facilityRental) throws Exception;
    // 게시글 수정
    public int update(FacilityRental facilityRental) throws Exception;
    // 게시글 삭제
    public int delete(int frNo) throws Exception;

    // 게시글 번호(기본키) 최대값
    public int maxPk() throws Exception;
}
