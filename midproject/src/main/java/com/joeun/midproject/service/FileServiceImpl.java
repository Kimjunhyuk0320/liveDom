package com.joeun.midproject.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.joeun.midproject.dto.Files;
import com.joeun.midproject.mapper.FileMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class FileServiceImpl implements FileService{
    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")            // application.properties 에 설정한 업로드 경로 속성명
    private String uploadPath;          // 업로드 경로

    @Override
    public List<Files> list() throws Exception {
        List<Files> fileList = fileMapper.list();
        return fileList;
    }

    @Override
    public Files select(int frNo) throws Exception {
        Files file = fileMapper.select(frNo);
        return file;
    }
    
    @Override
    public int insert(Files facilityRental) throws Exception {
        int result = fileMapper.insert(facilityRental);
        return result;
    }

    @Override
    public int update(Files facilityRental) throws Exception {
        int result = fileMapper.update(facilityRental);
        return result;
    }

    @Override
    public int delete(int frNo) throws Exception {
        int result = fileMapper.delete(frNo);
        return result;
    }

    @Override
    public List<Files> listByParent(Files file) throws Exception {
        List<Files> fileList = fileMapper.listByParent(file);
        return fileList;
    }

    @Override
    public int deleteByParent(Files file) throws Exception {
        int result = fileMapper.deleteByParent(file);
        return result;
    }

    @Override
    public int download(int fileNo, HttpServletResponse response) throws Exception {
        // result
        // 0 : 파일 다운로드 처리 실패
        // 1 : 파일 다운로드 성공
        Files file = fileMapper.select(fileNo);
        
        if(file == null) {
            // BAD_REQUEST : 400, 클라이언트의 요청이 잘못되었음을 알려주는 상태코드
            // response.setStatus(response.SC_BAD_REQUEST);
            return 0;
        }

        String filePath = file.getPath();       // 파일 경로
        String fileName = file.getFileName();       // 파일 이름

        // 다운로드 응답을 위한 헤더 세팅
        // - ContentType         : application/octet-stream
        // - Content-Disposition : attachment, filename="파일명.확장자"
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 파일 다운로드
        // - 파일 입력
        File downloadFile = new File(filePath);
        FileInputStream fis = new FileInputStream(downloadFile);

        // - 파일 출력
        ServletOutputStream sos = response.getOutputStream();

        // 다운로드
        FileCopyUtils.copy(fis, sos);
        
        // byte[] buffer = new byte[1024];      // 1024bytes = 1KB 단위 버퍼
        // int data;
        // While( (data = fis.read(buffer)) != -1 ) {   // 1KB 씩 파일입력
        //      sos.write(buffer, 0, data);         // 1KB 씩 파일출력
        //}
        fis.close();
        sos.close();

        return 1;
    }

    @Override
    public int thumbnail(int fileNo, HttpServletResponse response) throws Exception {
        // result
        // 0 : 파일 다운로드 처리 실패
        // 1 : 파일 다운로드 성공
        Files file = fileMapper.select(fileNo);
        if(file == null) {
            // BAD_REQUEST : 400, 클라이언트의 요청이 잘못되었음을 알려주는 상태코드
            // response.setStatus(response.SC_BAD_REQUEST);
            return 0;
        }

        String filePath = file.getPath();       // 파일 경로
        String fileName = file.getFileName();       // 파일 이름

        // 다운로드 응답을 위한 헤더 세팅
        // - ContentType         : application/octet-stream
        // - Content-Disposition : attachment, filename="파일명.확장자"
        int index = fileName.lastIndexOf(".");
        String ext = fileName.substring(index).toUpperCase();
        String mediaType = null;

        switch(ext){
            case "JPG":
            case "JPGE":
                        mediaType = MediaType.IMAGE_JPEG_VALUE;
                        break;
            case "GIF":
                        mediaType = MediaType.IMAGE_GIF_VALUE;
                        break;
            case "PNG":
                        mediaType = MediaType.IMAGE_PNG_VALUE;
                        break;
        }
        response.setContentType(mediaType);
        // response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 파일 다운로드
        // - 파일 입력
        File downloadFile = new File(filePath);
        FileInputStream fis = new FileInputStream(downloadFile);

        // - 파일 출력
        ServletOutputStream sos = response.getOutputStream();

        // 다운로드
        FileCopyUtils.copy(fis, sos);
        
        // byte[] buffer = new byte[1024];      // 1024bytes = 1KB 단위 버퍼
        // int data;
        // While( (data = fis.read(buffer)) != -1 ) {   // 1KB 씩 파일입력
        //      sos.write(buffer, 0, data);         // 1KB 씩 파일출력
        //}
        fis.close();
        sos.close();

        return 1;
    }

    @Override
    public int uploadImg(List<MultipartFile> file) throws Exception {
        // 파일 업로드 
        List<MultipartFile> fileList = file;
        int parentNo = 0;
        String parentTable = "editor";
        int fileNo = 0;
        if( !fileList.isEmpty() ) 
        for (MultipartFile files : fileList) {
            if( files.isEmpty() ) continue;

            // 파일 정보 : 원본파일명, 파일 용량, 파일 데이터 
            String originName = files.getOriginalFilename();
            long fileSize = files.getSize();
            byte[] fileData = files.getBytes();
            
            // 업로드 경로
            // 파일명 중복 방지 방법(정책)
            // - 날짜_파일명.확장자
            // - UID_파일명.확장자

            // UID_강아지.png
            String fileName = UUID.randomUUID().toString() + "_" + originName;

            // c:/upload/UID_강아지.png
            String filePath = uploadPath + "/" + fileName;

            // 파일업로드
            // - 서버 측, 파일 시스템에 파일 복사
            // - DB 에 파일 정보 등록
            File uploadFile = new File(uploadPath, fileName);
            FileCopyUtils.copy(fileData, uploadFile);       // 파일 업로드
            // FileOutputStream fos = new FileOutputStream(uploadFile);
            // fos.write(fileData);
            // fos.close();
            Files uploadedFile = new Files();
            uploadedFile.setParentTable(parentTable);
            uploadedFile.setParentNo(parentNo);
            uploadedFile.setFileName(fileName);
            uploadedFile.setPath(filePath);
            uploadedFile.setOriginName(originName);
            uploadedFile.setFileSize(fileSize);
            uploadedFile.setFileCode(1);
            fileMapper.insert(uploadedFile);
            fileNo = fileMapper.maxPk();
        }

        return fileNo;
    }
}
