package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.Files;
import com.joeun.midproject.dto.Users;

@Mapper
public interface FileMapper {
    // 게시글 목록
    public List<Files> list() throws Exception;
    // 게시글 조회
    public Files select(int fileNo) throws Exception;
    // 게시글 등록
    public int insert(Files file) throws Exception;
    // 썸네일 조회
    public Files selectThumbnail(Files file) throws Exception;
    // 게시글 수정
    public int update(Files file) throws Exception;
    // 게시글 삭제
    public int delete(int fileNo) throws Exception;
    // 파일 목록 - 부모 기준
    public List<Files> listByParent(Files file) throws Exception;
    // 파일 삭제 - 부모 기준
    public int deleteByParent(Files file) throws Exception;
    
    // 파일 최고 Pk 조회
    public int maxPk() throws Exception;

    public Files selectProfile(Users users)throws Exception;

}