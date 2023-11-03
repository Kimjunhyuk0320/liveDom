package com.joeun.midproject.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.joeun.midproject.dto.BookingRequests;
import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.Files;
import com.joeun.midproject.mapper.BookingRequestsMapper;
import com.joeun.midproject.mapper.FacilityRentalMapper;
import com.joeun.midproject.mapper.FileMapper;

@Service
public class FacilityRentalServiceImpl implements FacilityRentalService {
    @Autowired
    private FacilityRentalMapper facilityRentalMapper;

    @Autowired
    private BookingRequestsMapper bookingRequestsMapper;

    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")        // application.properties 에 설정한 업로드 경로 속성명
    private String uploadPath;  // = "D:/TJE/UPLOAD"; 예전에는 이렇게 변수에 담아서 사용했는데 이젠 위에 적어놓은 애플리케이션.프로퍼티스에 적어놓는다. // 업로드 경로

    // 게시글 목록(FacilityRentalMapper 에서 쿼리문으로 DB에서 데이터를 가져와서 조회)
    @Override
    public List<FacilityRental> list() throws Exception {
        List<FacilityRental> facilityList = facilityRentalMapper.list();
        return facilityList;
    }

    // 게시글 조회
    @Override
    public FacilityRental select(int frNo) throws Exception {
        FacilityRental facilityRental = facilityRentalMapper.select(frNo);
        // 조회수 증가... (어케 해야할까요?)

        return facilityRental;
    }

    // 게시글 등록
    @Override
    public int insert(FacilityRental facilityRental) throws Exception {
        int result = facilityRentalMapper.insert(facilityRental);
        String parentTable = "facilityRental";
        int parentNo = facilityRentalMapper.maxPk();

        // 파일 업로드
        List<MultipartFile> fileList = facilityRental.getFile();

        if( !fileList.isEmpty() )
        for(MultipartFile file : fileList) {
            if(file.isEmpty()) continue;

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
            // DB 에 파일 정보 등록
           
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


    // 게시글 수정
    @Override
    public int update(FacilityRental facilityRental) throws Exception {
        int result = facilityRentalMapper.update(facilityRental);
        return result;
    }


    // 게시글 삭제
    @Override
    public int delete(int frNo) throws Exception {
        int result = facilityRentalMapper.delete(frNo);
        return result;
    }

    // 대관 예약 신청
    @Override
    public int reservation(BookingRequests bookingRequests) throws Exception {
        int result = bookingRequestsMapper.reservation(bookingRequests);
        return result;
    }
}
