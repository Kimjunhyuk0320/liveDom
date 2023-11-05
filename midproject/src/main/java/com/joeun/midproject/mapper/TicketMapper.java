package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;

@Mapper
public interface TicketMapper {

    // 티켓 구매시 티켓 테이블에 등록
    public int insert(Ticket ticket) throws Exception;
    
    // 유저 전화번호로 구매한 티켓 목록 조회하기
    public List<Ticket> listByPhone(String phone) throws Exception;
    
    // 유저 아이디로 작성한 게시글에 대한 티켓 판매 목록 조회하기
    public List<Ticket> listByUserName(String username) throws Exception;
    
    // 게시글 번호로 판매한 티켓 목록 조회하기
    public List<Ticket> listByBoardNo(int boardNo) throws Exception;

}
