package com.joeun.midproject.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.joeun.midproject.dto.BookingRequests;
import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.Files;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.mapper.BookingRequestsMapper;
import com.joeun.midproject.mapper.FacilityRentalMapper;
import com.joeun.midproject.mapper.FileMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacilityRentalServiceImpl implements FacilityRentalService {
    @Autowired
    private FacilityRentalMapper facilityRentalMapper;

    @Autowired
    private BookingRequestsMapper bookingRequestsMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")        // application.properties 에 설정한 업로드 경로 속성명
    private String uploadPath;  // = "D:/TJE/UPLOAD"; 예전에는 이렇게 변수에 담아서 사용했는데 이젠 위에 적어놓은 애플리케이션.프로퍼티스에 적어놓는다. // 업로드 경로

    // 게시글 목록(FacilityRentalMapper 에서 쿼리문으로 DB에서 데이터를 가져와서 조회)
    @Override
    public List<FacilityRental> list() throws Exception {
        List<FacilityRental> facilityList = facilityRentalMapper.list();
        for (FacilityRental facilityRental : facilityList) {
            Files files = new Files();
            files.setParentNo(facilityRental.getFrNo());
            files.setParentTable("facility_rental");
            facilityRental.setThumbnail(fileMapper.selectThumbnail(files));
        }
        return facilityList;
    }

    // 게시글 조회
    @Override
    public FacilityRental select(int frNo) throws Exception {
        FacilityRental facilityRental = facilityRentalMapper.select(frNo);
        if(facilityRental!=null){
            facilityRentalMapper.viewsUp(frNo);
            Files files = new Files();
            files.setParentNo(facilityRental.getFrNo());
            files.setParentTable("facility_rental");
            facilityRental.setThumbnail(fileMapper.selectThumbnail(files));
        }
            
        return facilityRental;
    }

    // 게시글 등록
    @Override
    public int insert(FacilityRental facilityRental) throws Exception {
        int result = facilityRentalMapper.insert(facilityRental);
        String parentTable = "facility_rental";
        int parentNo = facilityRentalMapper.maxPk();

        // 파일 업로드
        List<MultipartFile> fileList = facilityRental.getFile();

        if(result>0&&fileList!=null&&!fileList.isEmpty())
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
        List<MultipartFile> fileList = facilityRental.getFile();

        String parentTable = "facility_rental";
        int parentNo = facilityRental.getFrNo();

        if(fileList!=null&&!fileList.isEmpty())
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

            //기존 썸네일 이미지 파일을 삭제해줘야합니다.
            //여기
            log.info(uploadFile.toString());
            //file테이블에 새로운 썸네일 등록
            int updateResult = fileMapper.update(uploadedFile);
            System.out.println(updateResult);
        }
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
        // 받은 신청 들어왔다고 문자 알려주기
        int frNo = bookingRequests.getFrNo();
        FacilityRental facilityRental = facilityRentalMapper.select(frNo);
        MultiValueMap<String, String> map =  new LinkedMultiValueMap<>();
        // ✅ 필수 정보
        // - receiver       :   1) 01012341234
        //                      2) 01011112222,01033334444
        // - msg            : 문자 메시지 내용
        // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
        // receiver에 문자 받는 사람의 전화번호를 넣어주세요.
        String receiver = facilityRental.getPhone();
        String msg = "LiveDom 대관 서비스\n" +"1개의 대관신청이 도착했습니다. 웹사이트를 방문해 확인해주시기 바랍니다.";
        String testmode_yn = "Y";
        map.add("receiver", receiver);
        map.add("msg", msg);
        map.add("testmode_yn", testmode_yn);

        log.info("메세지 발송 테스트 333 메세지 : " + msg + " 전화번호 : "+ receiver);
        Map<String, Object> resultMap = smsService.send(map);
        log.info(resultMap + "");
        Object resultCode = resultMap.get("result_code");
        Integer result_code = Integer.valueOf( resultCode != null ? resultCode.toString() : "-1" );
        String message = (String) resultMap.get("message");
        if( result_code == 1 )
            log.info("문자 발송 성공 : " + message);
        if( result_code == -1 )
            log.info("문자 발송 실패 : " + message);
  
  







        return result;
    }

    @Override
    public List<BookingRequests> rrList(String username) throws Exception {

        List<BookingRequests> rrList = bookingRequestsMapper.rentalList(username);

        return rrList;

    }

    @Override
    public int delReq(BookingRequests bookingRequests) throws Exception {


        int result = bookingRequestsMapper.delReq(bookingRequests);

        return result;
    }

    @Override
    public List<BookingRequests> rreqList(String username) throws Exception {

        List<BookingRequests> rreqList = bookingRequestsMapper.rreqList(username);

        return rreqList;
    }

    @Override
    public int reqDenied(BookingRequests bookingRequests) throws Exception {

        int result = bookingRequestsMapper.reqDenied(bookingRequests);

        return result;

    }

    @Override
    public int reqAccept(BookingRequests bookingRequests) throws Exception {

        int result = bookingRequestsMapper.reqAccept(bookingRequests);
        int br_no = bookingRequests.getBrNo();
        BookingRequests br = bookingRequestsMapper.listBybrNo(br_no);
        int fr_no = br.getFrNo();
        FacilityRental facilityRental = facilityRentalMapper.select(fr_no);
        // 대관 신청자에게 결제정보 보내기.
        MultiValueMap<String, String> map =  new LinkedMultiValueMap<>();
        // ✅ 필수 정보
        // - receiver       :   1) 01012341234
        //                      2) 01011112222,01033334444
        // - msg            : 문자 메시지 내용
        // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
        // receiver에 문자 받는 사람의 전화번호를 넣어주세요.
        // bank에 입금 받아야하는 계좌번호를 넣어주세요.
        // price 에 입금 금액을 입력해주세요.
        String receiver = facilityRental.getPhone();
        String bank = facilityRental.getAccount();
        Integer price = facilityRental.getPrice();
        String msg = "LiveDom 대관 서비스\n" +"대관 신청이 승인되었습니다." + bank + "로" + price + "원을 입금해주시기 바랍니다.";
        String testmode_yn = "Y";
        map.add("receiver", receiver);
        map.add("msg", msg);
        map.add("testmode_yn", testmode_yn);

        log.info("메세지 발송 테스트 333 메세지 : " + msg + " 전화번호 : "+ receiver);
        Map<String, Object> resultMap = smsService.send(map);
        log.info(resultMap + "");
        Object resultCode = resultMap.get("result_code");
        Integer result_code = Integer.valueOf( resultCode != null ? resultCode.toString() : "-1" );
        String message = (String) resultMap.get("message");
        if( result_code == 1 )
            log.info("문자 발송 성공 : " + message);
        if( result_code == -1 )
            log.info("문자 발송 실패 : " + message);
  
  



        return result;
    }
    
    @Override
    public int reqConfirm(BookingRequests bookingRequests) throws Exception {
        
        int resultReqConfirm = 0;
         resultReqConfirm = bookingRequestsMapper.reqConfirm(bookingRequests);
        int brNo = bookingRequests.getBrNo();
        BookingRequests bookingRequests2 = bookingRequestsMapper.listBybrNo(brNo);
        int frNo = bookingRequests2.getFrNo();
        FacilityRental facilityRental1 = facilityRentalMapper.select(frNo);

        if(resultReqConfirm>0){
            // 입금 확인되었다고 메세지 보내기
             MultiValueMap<String, String> map1 =  new LinkedMultiValueMap<>();
            // ✅ 필수 정보
            // - receiver       :   1) 01012341234
            //                      2) 01011112222,01033334444
            // - msg            : 문자 메시지 내용
            // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
            // receiver에 문자 받는 사람들의 전화번호를 "," 로 연결해서 넣어주세요.
            // Title에 게시글의 제목을 입력해주세요
            // liveDate에 공연일자를 입력해주세요.
            // address에 공연장의 위치를 입력해주세요.
            String receiver1 = bookingRequests2.getPhone();
            String title1 = facilityRental1.getTitle();
            String liveDate1 = facilityRental1.getLiveDate();
            String address1 = facilityRental1.getAddress();
            
            String msg1 = "LiveDom 대관 서비스\n\"" + title1 + "\"에 대한 대관료 입금이 획인되었습니다 \n" +
            "공연장 : " + address1 + "\n대관일자 : " + liveDate1;
            String testmode_yn1 = "Y";
            map1.add("receiver", receiver1);
            map1.add("msg", msg1);
            map1.add("testmode_yn", testmode_yn1);



            log.info("메세지 발송 테스트 333 메세지 : " + msg1 + " 전화번호 : "+ receiver1);
            Map<String, Object> resultMap1 = smsService.send(map1);
            log.info(resultMap1 + "");
            Object resultCode1 = resultMap1.get("result_code");
            Integer result_code1 = Integer.valueOf( resultCode1 != null ? resultCode1.toString() : "-1" );
            String message1 = (String) resultMap1.get("message");
            if( result_code1 == 1 )
                log.info("문자 발송 성공 : " + message1);
            if( result_code1 == -1 )
                log.info("문자 발송 실패 : " + message1);












            
            int br_no = bookingRequests.getBrNo();
            BookingRequests br = bookingRequestsMapper.listBybrNo(br_no);
            int fr_no = br.getFrNo();
            bookingRequests.setFrNo(fr_no);
        // 대관 신청자에게 예약완료되었다고 메세지 보내기
        FacilityRental facilityRental = facilityRentalMapper.select(fr_no);
        MultiValueMap<String, String> map =  new LinkedMultiValueMap<>();
        // ✅ 필수 정보
        // - receiver       :   1) 01012341234
        //                      2) 01011112222,01033334444
        // - msg            : 문자 메시지 내용
        // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
        // receiver에 문자 받는 사람들의 전화번호를 "," 로 연결해서 넣어주세요.
        // Title에 게시글의 제목을 입력해주세요
        // liveDate에 공연일자를 입력해주세요.
        // address에 공연장의 위치를 입력해주세요.
        String receiver = br.getPhone();
        String title = facilityRental.getTitle();
        String liveDate = facilityRental.getLiveDate();
        String address = facilityRental.getAddress();
        
        String msg = "LiveDom 대관 서비스\n\"" + title + "\"의 대관이 성사되었습니다. \n" +
        "공연장 : " + address + "\n대관일자 : " + liveDate;
        String testmode_yn = "Y";
        map.add("receiver", receiver);
        map.add("msg", msg);
        map.add("testmode_yn", testmode_yn);



        log.info("메세지 발송 테스트 333 메세지 : " + msg + " 전화번호 : "+ receiver);
        Map<String, Object> resultMap = smsService.send(map);
        log.info(resultMap + "");
        Object resultCode = resultMap.get("result_code");
        Integer result_code = Integer.valueOf( resultCode != null ? resultCode.toString() : "-1" );
        String message = (String) resultMap.get("message");
        if( result_code == 1 )
            log.info("문자 발송 성공 : " + message);
        if( result_code == -1 )
            log.info("문자 발송 실패 : " + message);
  
  








        
            resultReqConfirm += bookingRequestsMapper.reqDeniedAll(bookingRequests);
            resultReqConfirm += bookingRequestsMapper.confirmUsername(bookingRequests);
        }

        return resultReqConfirm;
    }

    @Override
    public List<FacilityRental> pageFrList(Team team) throws Exception {
        if(team.getPageNo()!=0){
        team.setPageNo((team.getPageNo()-1)*team.getRows());
        }
        List<FacilityRental> pageFrList = facilityRentalMapper.pageFrList
        (team);

        for (FacilityRental facilityRental : pageFrList) {
            Files files = new Files();
            files.setParentNo(facilityRental.getFrNo());
            files.setParentTable("facility_rental");
            facilityRental.setThumbnail(fileMapper.selectThumbnail(files));
        }

        return pageFrList;
    }
}
