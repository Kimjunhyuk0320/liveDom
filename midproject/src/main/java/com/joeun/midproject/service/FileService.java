package com.joeun.midproject.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.joeun.midproject.dto.Files;

public interface FileService {
    
    //파일 목록
    public List<Files> list() throws Exception;
    // 파일 조회
    public Files select(int frNo) throws Exception;
    // 파일 등록
    public int insert(Files facilityRental) throws Exception;
    // 파일 수정
    public int update(Files facilityRental) throws Exception;
    // 파일 삭제
    public int delete(int frNo) throws Exception;
    // 썸네일 불러오기
    public int thumbnail(int fileNo, HttpServletResponse response) throws Exception;
   
    // 파일 목록 - 부모 기준
    public List<Files> listByParent(Files file) throws Exception;
    // 파일 삭제 - 부모 기준
    public int deleteByParent(Files file) throws Exception;
    
    // 파일 다운로드
    public int download(int fileNo, HttpServletResponse response) throws Exception;


    //  써머노트 이미지 파일 업로드
    public int uploadImg(List<MultipartFile> file) throws Exception;
    
    
}
