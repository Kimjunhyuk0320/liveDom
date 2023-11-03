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
    
    

    
    
   
 

}
