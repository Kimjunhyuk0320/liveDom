package com.joeun.midproject.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.joeun.midproject.dto.Files;
import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;
import com.joeun.midproject.mapper.FileMapper;
import com.joeun.midproject.mapper.LiveBoardMapper;
import com.joeun.midproject.mapper.TicketMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LiveBoardServiceImpl implements LiveBoardService{
    @Autowired
    LiveBoardMapper liveBoardMapper;

    @Autowired
    TicketMapper ticketMapper;

    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")            // application.properties 에 설정한 업로드 경로 속성명
    private String uploadPath;          // 업로드 경로

    @Override
    public int insert(LiveBoard liveBoard) throws Exception {
        int result = liveBoardMapper.insert(liveBoard);
        String parentTable = "live_board";
        int parentNo = liveBoardMapper.maxPk();

        // 파일 업로드 
        List<MultipartFile> fileList = liveBoard.getFile();

        if( !fileList.isEmpty() )
        for (MultipartFile file : fileList) {

            if( file.isEmpty() ) continue;

            // 파일 정보 : 원본파일명, 파일 용량, 파일 데이터 
            String originName = file.getOriginalFilename();
            long fileSize = file.getSize();
            byte[] fileData = file.getBytes();
            
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
            uploadedFile.setFileCode(0);

            fileMapper.insert(uploadedFile);
        }









        return result;
    }

    @Override
    public int update(LiveBoard liveBoard) throws Exception {
        int result = liveBoardMapper.update(liveBoard);

        String parentTable = "live_board";
        int parentNo = liveBoard.getBoardNo();

        // 파일 업로드 
        List<MultipartFile> fileList = liveBoard.getFile();

        if( !fileList.isEmpty() )
        for (MultipartFile file : fileList) {

            if( file.isEmpty() ) continue;

            // 파일 정보 : 원본파일명, 파일 용량, 파일 데이터 
            String originName = file.getOriginalFilename();
            long fileSize = file.getSize();
            byte[] fileData = file.getBytes();
            
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
            Files beforeFile = fileMapper.selectThumbnail(uploadedFile);
            uploadedFile.setFileName(fileName);
            uploadedFile.setPath(filePath);
            uploadedFile.setOriginName(originName);
            uploadedFile.setFileSize(fileSize);
            uploadedFile.setFileCode(0);
            uploadedFile.setFileNo(beforeFile.getFileNo());
            fileMapper.update(uploadedFile);
        }







        log.info(result + "");
        return result;
    }

    @Override
    public LiveBoard select(int boardNo) throws Exception {
        LiveBoard liveBoard = liveBoardMapper.select(boardNo);
        Files file = new Files();
        file.setParentTable("live_board");
        file.setParentNo(boardNo);
        file = fileMapper.selectThumbnail(file);
        liveBoard.setThumbnail(file);
        return liveBoard;
    }

    @Override
    public List<LiveBoard> list() throws Exception {
        List<LiveBoard> liveBoardList = liveBoardMapper.list();
        for(int i = 0; i <liveBoardList.size() ; i++){
            LiveBoard liveBoard = liveBoardList.get(i);
             Files file = new Files();
            file.setParentTable("live_board");
            file.setParentNo(liveBoard.getBoardNo());
            file = fileMapper.selectThumbnail(file);
            liveBoard.setThumbnail(file);
        }
        return liveBoardList;
    }

    @Override
    public int soldOut(int boardNo) throws Exception{
        int result = liveBoardMapper.soldOut(boardNo);
        return result;
    }

    @Override
    public int purchase(Ticket ticket) throws Exception {
        String reservationUuid = UUID.randomUUID().toString();
        String reservationNo = "T" + reservationUuid;
        ticket.setReservationNo(reservationNo);
        int result = ticketMapper.insert(ticket);
        return result;
    }

    @Override
    public List<Ticket> listByBoardNo(int boardNo) throws Exception {
       List<Ticket> ticketList = ticketMapper.listByBoardNo(boardNo);
       return ticketList;
    }

    
    
}
