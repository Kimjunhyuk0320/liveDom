package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.Team;

@Mapper
public interface LiveBoardMapper {

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

    // 게시글 목록 조회(paging)
    public List<LiveBoard> liveBoardPageList(Team team) throws Exception;
    
    // 매진으로 변경
    public int soldOut(int boardNo);
    
    // 게시글 번호(기본키) 최댓값
    public int maxPk();
 

}
