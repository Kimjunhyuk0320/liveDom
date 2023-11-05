package com.joeun.midproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;


public interface TeamService {
  
  public List<Team> list();

  public int insert(Team team);

  public int update(Team team);

  public int delete(Team team);

  public Team read(Team team);  

  public List<Team> listByConfirmedLive(String username);

  public List<Team> listByConfirmedLive2(Team team);

  public PageInfo pageInfo(PageInfo pageInfo);

  public List<Team> pageList(Team team);

}
