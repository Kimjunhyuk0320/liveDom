package com.joeun.midproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.Team;


public interface TeamService {
  
  public List<Team> list();

  public int insert(Team team);

  public int update(Team team);

  public int delete(Team team);

  public Team read(Team team);  
}