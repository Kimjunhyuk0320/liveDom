package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.TeamApp;

@Mapper
public interface TeamAppMapper {

  
  public int insert(TeamApp teamApp);
  
  public List<TeamApp> listByLeader(TeamApp teamApp);

  public List<TeamApp> listByMember(TeamApp teamApp);

  public int delete(TeamApp teamApp);

  public TeamApp read(TeamApp teamApp);

  public int accept(TeamApp teamApp);

  public int denied(TeamApp teamApp);

  public int deniedAll(TeamApp teamApp);

  public int confirmed(TeamApp teamApp);

  public List<TeamApp> listByTeamNo(int teamNo);
  
  public int insertLive(TeamApp teamApp);



}
