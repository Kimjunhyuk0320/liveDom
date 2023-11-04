package com.joeun.midproject.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.Users;

@Mapper
public interface UserMapper {

    // 회원 등록
    public int insert(Users users);

    // 회원 수정
    public int update(Users user);
    
    // 회원 조회
    public Users read(String username);

    // 회원 닉네임 조회
    public Users readOnlyNickname(String nickname);

    // 회원 연락처 조회
    public Users readOnlyPhone(String phone);
    
    

    
    
   
 

}
