package com.joeun.midproject.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.Users;

@Mapper
public interface UserTempMapper {

  public int insert(Users users);

  public Users read(String username);
  
}
