package com.joeun.midproject.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import com.joeun.midproject.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private TicketMapper ticketMapper;

  @Autowired
  private LiveBoardMapper liveBoardMapper;

  @Autowired
  private FileMapper fileMapper;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Value("${upload.path}")
  private String uploadPath;





  // 유저 조회
  @Override
  public Users read(String username) {
      return userMapper.read(username);
  }

  // 유저 닉네임 조회
  @Override
  public Users readOnlyNickname(String nickname) {
    return userMapper.readOnlyNickname(nickname);
  }
  
  // 유저 연락처 조회
  @Override
  public Users readOnlyPhone(String phone) {
    return userMapper.readOnlyPhone(phone);
  }

  // 회원가입
  @Override
  public int insert(Users users,HttpServletRequest request) throws Exception{

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userMapper.insert(users);

    if(result>0){
      // 파일 업로드 
        MultipartFile file = users.getFile();
      
        if(file!=null&&!file.isEmpty()){

        


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
            uploadedFile.setParentTable("users");
            uploadedFile.setParentUsername(users.getUsername());
            uploadedFile.setFileName(fileName);
            uploadedFile.setPath(filePath);
            uploadedFile.setOriginName(originName);
            uploadedFile.setFileSize(fileSize);
            uploadedFile.setFileCode(2);
            //파일DB등록
            fileMapper.insert(uploadedFile);

            //유저DB에서 방금등록한 fileNo가져와 객체에 담기
            users.setProfileNo(fileMapper.maxPk());
            userMapper.profileSet(users);
    }
    //바로 로그인 진행
    String username = users.getUsername();
    String password = users.getUserPwCheck();

    // 아이디, 패스워드 인증 토큰 생성
    UsernamePasswordAuthenticationToken token 
        = new UsernamePasswordAuthenticationToken(username, password);

    // 토큰에 요청정보를 등록
    token.setDetails( new WebAuthenticationDetails(request) );

    // 토큰을 이용하여 인증(로그인)
    Authentication authentication = authenticationManager.authenticate(token);


    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
  return result;
}

  // 회원 정보 수정
  @Override
  public int update(Users users,HttpServletRequest request,HttpServletResponse response) throws Exception{

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userMapper.update(users);

    if(result>0){
      // 파일 업로드 
        MultipartFile file = users.getFile();
      
        if(file!=null&&!file.isEmpty()){

        


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
            uploadedFile.setParentTable("users");
            uploadedFile.setParentUsername(users.getUsername());
            uploadedFile.setFileName(fileName);
            uploadedFile.setPath(filePath);
            uploadedFile.setOriginName(originName);
            uploadedFile.setFileSize(fileSize);
            uploadedFile.setFileCode(2);
            //파일DB등록
            fileMapper.insert(uploadedFile);
            
            Integer preProfileNo =  userMapper.read(users.getUsername()).getProfileNo();

            if(preProfileNo != null){

              fileMapper.delete(preProfileNo);

            }

            //유저DB에서 방금등록한 fileNo가져와 객체에 담기
            users.setProfileNo(fileMapper.maxPk());
            userMapper.profileSet(users);
    }

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if(auth!=null){

      new SecurityContextLogoutHandler().logout(request, response, auth);

    }

  }

    return result;

  }

  @Override
  public List<Ticket> listByPhone(Users users) throws Exception {
    String phone = users.getPhone();
    List<Ticket> ticketList = ticketMapper.listByPhone(phone);
    for(int i = 0 ; i < ticketList.size(); i++){
      int boardNo = ticketList.get(i).getBoardNo();
      LiveBoard LiveBoard = liveBoardMapper.select(boardNo);
      ticketList.get(i).setTitle(LiveBoard.getTitle());
      ticketList.get(i).setLiveDate(LiveBoard.getLiveDate());
    }
    return ticketList;
  }

  @Override
  public List<Ticket> listByUserName(Users users) throws Exception{
    String username = users.getUsername();
    List<Ticket> ticketList = ticketMapper.listByUserName(username);
    
    return ticketList;
  }



  
}
