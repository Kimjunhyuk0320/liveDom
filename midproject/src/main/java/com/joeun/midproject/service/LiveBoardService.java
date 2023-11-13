package com.joeun.midproject.service;

import java.util.List;


import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;


public interface LiveBoardService {
     // 게시글 등록
    public int insert(LiveBoard liveBoard) throws Exception;
    
    // 게시글 수정
    public int update(LiveBoard liveBoard) throws Exception;
    
    // 게시글 삭제
    public int delete(LiveBoard liveBoard) throws Exception;
    
    // 게시글 조회
    public LiveBoard select(int boardNo) throws Exception;

    // 게시글 목록 조회
    public List<LiveBoard> list() throws Exception;

    public List<LiveBoard> liveBoardPageList(Team team) throws Exception;
    
    // 매진으로 변경
    public int soldOut(int boardNo) throws Exception;
    
    // 티켓 구매
    public int purchase(Ticket ticket) throws Exception;

    // 게시글 번호로 판매한 티켓 목록 조회하기
    public List<Ticket> listByBoardNo(int boardNo) throws Exception;


}
