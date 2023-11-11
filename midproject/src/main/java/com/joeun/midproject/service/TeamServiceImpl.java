package com.joeun.midproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.mapper.TeamMapper;

@Service
public class TeamServiceImpl implements TeamService{

  @Autowired
  private TeamMapper teamMapper;

  @Override
  public List<Team> list() {

    List<Team> teamList = teamMapper.list();

    return teamList;

  }

  @Override
  public int insert(Team team) {

    int result = teamMapper.insert(team);

    return result;
  }

  @Override
  public int update(Team team) {

    int result = teamMapper.update(team);


    return result;
  }
  
  @Override
  public int delete(Team team) {
    int result = teamMapper.delete(team);
  
  
    return result;
  }
  
  @Override
  public Team read(Team team) {

    int resultView = teamMapper.addView(team);
    Team resultTeam = new Team();

    if(resultView>0){

      resultTeam = teamMapper.read(team);
      return resultTeam;
    }
    else{

      return resultTeam;

    }

  
  
  }

  @Override
  public List<Team> listByConfirmedLive(String username) {

    List<Team> listByConfirmedList = teamMapper.listByConfirmedLive(username);

    return listByConfirmedList;
    
  }

  @Override
  public PageInfo pageInfo(PageInfo pageInfo) {

    PageInfo pageInfoResult = teamMapper.pageInfo(pageInfo);

    return pageInfoResult;
  }

  @Override
  public List<Team> pageList(Team team) {

    if(team.getPageNo()!=0){
    team.setPageNo((team.getPageNo()-1)*team.getRows());
    }
    List<Team> teamList = teamMapper.pageList(team);

    return teamList;

  }

  @Override
  public List<Team> listByConfirmedLive2(Team team) {

    team.setPageNo((team.getPageNo()-1)*team.getRows());

    List<Team> teamList = teamMapper.listByConfirmedLive2(team);

    return teamList;


  }


  
}
