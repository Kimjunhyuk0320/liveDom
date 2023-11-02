package com.joeun.midproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.TeamApp;
import com.joeun.midproject.mapper.TeamAppMapper;
import com.joeun.midproject.mapper.TeamMapper;

@Service
public class TeamAppServiceImpl implements TeamAppService{

  @Autowired
  private TeamAppMapper teamAppMapper;

  @Autowired
  private TeamMapper teamMapper;


  @Override
  public int insert(TeamApp teamApp) {

    int result = teamAppMapper.insert(teamApp);

    return result;
  }

  @Override
  public List<TeamApp> listByLeader(TeamApp teamApp) {

    List<TeamApp> listByLider = teamAppMapper.listByLeader(teamApp);

    return listByLider;

  }

  @Override
  public List<TeamApp> listByMember(TeamApp teamApp) {

     List<TeamApp> listByMember = teamAppMapper.listByMember(teamApp);


    return listByMember;

  }

  @Override
  public TeamApp read(TeamApp teamApp) {

     TeamApp readResult = teamAppMapper.read(teamApp);

    return readResult;
  }

  @Override
  public int accept(TeamApp teamApp) {

     int result = teamAppMapper.accept(teamApp);

    return result;
  }

  @Override
  public int denied(TeamApp teamApp) {

    int result = teamAppMapper.denied(teamApp);

    int deniedAllResult = 0;

    Team tempTeam = new Team();
    tempTeam.setTeamNo(teamApp.getTeamNo());

    Team team = teamMapper.read(tempTeam);

    if(team.getRecStatus()==team.getCapacity()){

      deniedAllResult = teamAppMapper.deniedAll(teamApp);

    }

    return result+deniedAllResult;
  }



  @Override
  public int confirmed(TeamApp teamApp) {

     int result = teamAppMapper.confirmed(teamApp);

    int deniedAllResult = 0;

    Team tempTeam = new Team();
    tempTeam.setTeamNo(teamApp.getTeamNo());

    Team team = teamMapper.read(tempTeam);

    if(team.getRecStatus()==team.getCapacity()){

      //공연성사테이블에 추가하는 로직을 구상해야합니다.
      //Team객체에 필요한 속성을 2개 추가로 구성했습니다.
      //crew의 경우, 해당 참여자들의 밴드명을 모두 찍어야합니다.

      List<TeamApp> confirmedTeamAppList = teamAppMapper.listByTeamNo(tempTeam.getTeamNo());
      String members = "";
      for(int i = 0; i<confirmedTeamAppList.size();i++){
        if(i<confirmedTeamAppList.size()-1){
          members += (confirmedTeamAppList.get(i).getBandName() + ", ");
        }else{
          members += confirmedTeamAppList.get(i).getBandName();

        }
        
      }
      
      teamApp.setMembers(members);

      teamAppMapper.insertLive(teamApp);


      deniedAllResult = teamAppMapper.deniedAll(teamApp);

    }

    return result+deniedAllResult;
    
  }

  @Override
  public int delete(TeamApp teamApp) {

    int result = teamAppMapper.delete(teamApp);


    return result;
  }
  
}
