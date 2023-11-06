package com.joeun.midproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;
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
  public int insert(Users users) {

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userMapper.insert(users);

    return result;
  }

  // 회원 정보 수정
  @Override
  public int update(Users users) {

    users.setPassword(passwordEncoder.encode(users.getPassword()));

    int result = userMapper.update(users);

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
