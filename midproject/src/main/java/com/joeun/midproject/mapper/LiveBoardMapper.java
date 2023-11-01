package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.LiveBoard;

@Mapper
public interface LiveBoardMapper {

    // 게시글 등록
    public int insert(LiveBoard liveBoard) throws Exception;
    
    // 게시글 수정
    public int update(LiveBoard liveBoard) throws Exception;
    
    // 게시글 조회
    public LiveBoard select(int boardNo) throws Exception;

    // 게시글 목록 조회
    public List<LiveBoard> list() throws Exception;
    
    // 매진으로 변경
    public int soldOut(int boardNo);
 

}
