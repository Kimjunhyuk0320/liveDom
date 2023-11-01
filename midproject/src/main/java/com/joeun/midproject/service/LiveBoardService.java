package com.joeun.midproject.service;

import java.util.List;


import com.joeun.midproject.dto.LiveBoard;


public interface LiveBoardService {
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
