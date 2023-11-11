package com.joeun.midproject.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.TeamApp;
import com.joeun.midproject.dto.Users;
import com.joeun.midproject.mapper.TeamAppMapper;
import com.joeun.midproject.mapper.TeamMapper;
import com.joeun.midproject.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class TeamAppServiceImpl implements TeamAppService{

  @Autowired
  private TeamAppMapper teamAppMapper;

  @Autowired
  private TeamMapper teamMapper;


  @Autowired
  private UserMapper userMapper;

  @Autowired
  private SMSService smsService;

  @Override
  public int insert(TeamApp teamApp) {

    int result = teamAppMapper.insert(teamApp);
      int teamNo = teamApp.getTeamNo();
      Team team1 = new Team();
      team1.setTeamNo(teamNo);
      Team team = teamMapper.read(team1);
      String id = team.getUsername();
      Users users = userMapper.read(id);
      String phone = users.getPhone();
     MultiValueMap<String, String> map =  new LinkedMultiValueMap<>();
      // ✅ 필수 정보
      // - receiver       :   1) 01012341234
      //                      2) 01011112222,01033334444
      // - msg            : 문자 메시지 내용
      // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
      // receiver에 문자 받는 사람의 전화번호를 넣어주세요.
      String receiver = phone;
      String msg = "LiveDom 팀 모집 서비스\n" +"1개의 참가신청서가 도착했습니다. 웹사이트를 방문해 확인해주시기 바랍니다.";
      String testmode_yn = "Y";
      map.add("receiver", receiver);
      map.add("msg", msg);
      map.add("testmode_yn", testmode_yn);

      log.info("메세지 발송 테스트 333 메세지 : " + msg + " 전화번호 : "+ receiver);
      Map<String, Object> resultMap = smsService.send(map);
      log.info(resultMap + "");
      Object resultCode = resultMap.get("result_code");
      Integer result_code = Integer.valueOf( resultCode != null ? resultCode.toString() : "-1" );
      String message = (String) resultMap.get("message");
      if( result_code == 1 )
          log.info("문자 발송 성공 : " + message);
      if( result_code == -1 )
          log.info("문자 발송 실패 : " + message);








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
     TeamApp teamApps = teamAppMapper.read(teamApp);
     int teamNo = teamApps.getTeamNo();
     String phone = teamApps.getPhone();
     Team teams = new Team();
     teams.setTeamNo(teamNo);
     Team team = teamMapper.read(teams);
     String account = team.getAccount();
     String title = team.getTitle();
      MultiValueMap<String, String> map =  new LinkedMultiValueMap<>();
      // ✅ 필수 정보
      // - receiver       :   1) 01012341234
      //                      2) 01011112222,01033334444
      // - msg            : 문자 메시지 내용
      // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
      // receiver에 문자 받는 사람의 전화번호를 넣어주세요.
      // bank에 입금 받아야하는 계좌번호를 넣어주세요.
      // price 에 입금 금액을 입력해주세요.
      String receiver = phone;
      String bank = account;
      Integer price = team.getPrice();
      String msg = "LiveDom 팀 모집 서비스\n" + "\"" +title + "\"에 대한 참가 신청이 승인되었습니다.\n" + bank + "로" + price + "원을 입금해주시기 바랍니다.";
      String testmode_yn = "Y";
      map.add("receiver", receiver);
      map.add("msg", msg);
      map.add("testmode_yn", testmode_yn);

      log.info("메세지 발송 테스트 333 메세지 : " + msg + " 전화번호 : "+ receiver);
      Map<String, Object> resultMap = smsService.send(map);
      log.info(resultMap + "");
      Object resultCode = resultMap.get("result_code");
      Integer result_code = Integer.valueOf( resultCode != null ? resultCode.toString() : "-1" );
      String message = (String) resultMap.get("message");
      if( result_code == 1 )
          log.info("문자 발송 성공 : " + message);
      if( result_code == -1 )
          log.info("문자 발송 실패 : " + message);







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

    //해당신청서 확정
    int result = teamAppMapper.confirmed(teamApp);

    TeamApp teamApp2 = teamAppMapper.read(teamApp);
    int teamNo = teamApp2.getTeamNo();
    Team team1 = new Team();
    team1.setTeamNo(teamNo);
    Team team2 = teamMapper.read(team1);

    // 입금 확인되었다고 메세지 보내기
    MultiValueMap<String, String> map1 =  new LinkedMultiValueMap<>();
    // ✅ 필수 정보
    // - receiver       :   1) 01012341234
    //                      2) 01011112222,01033334444
    // - msg            : 문자 메시지 내용
    // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
    // receiver에 문자 받는 사람들의 전화번호를 "," 로 연결해서 넣어주세요.
    // Title에 게시글의 제목을 입력해주세요
    // liveDate에 공연일자를 입력해주세요.
    // address에 공연장의 위치를 입력해주세요.
    String receiver1 = teamApp2.getPhone();
    String title1 = team2.getTitle();
    String liveDate1 = team2.getLiveDate();
    String address1 = team2.getAddress();
    
    String msg1 = "LiveDom 팀 모집 서비스\n\"" + title1 + "\"에 대한 입금이 획인되었습니다 \n" +
    "공연장 : " + address1 + "\n대관일자 : " + liveDate1;
    String testmode_yn1 = "Y";
    map1.add("receiver", receiver1);
    map1.add("msg", msg1);
    map1.add("testmode_yn", testmode_yn1);



    log.info("메세지 발송 테스트 333 메세지 : " + msg1 + " 전화번호 : "+ receiver1);
    Map<String, Object> resultMap1 = smsService.send(map1);
    log.info(resultMap1 + "");
    Object resultCode1 = resultMap1.get("result_code");
    Integer result_code1 = Integer.valueOf( resultCode1 != null ? resultCode1.toString() : "-1" );
    String message1 = (String) resultMap1.get("message");
    if( result_code1 == 1 )
        log.info("문자 발송 성공 : " + message1);
    if( result_code1 == -1 )
        log.info("문자 발송 실패 : " + message1);


    int deniedAllResult = 0;

    Team tempTeam = new Team();
    tempTeam.setTeamNo(teamApp.getTeamNo());

    //해당 팀 데이터 조회
    Team team = teamMapper.read(tempTeam);

    if(team.getRecStatus()==team.getCapacity()){

      //해당 팀 모집 종료 상태로 변경
      teamMapper.confirmed(team);
      //공연성사테이블에 추가하는 로직을 구상해야합니다.
      //Team객체에 필요한 속성을 2개 추가로 구성했습니다.
      //crew의 경우, 해당 참여자들의 밴드명을 모두 찍어야합니다.

      List<TeamApp> confirmedTeamAppList = teamAppMapper.listByTeamNo(tempTeam.getTeamNo());
      
      String members = userMapper.read(team.getUsername()).getNickname()+", ";
      for(int i = 0; i<confirmedTeamAppList.size();i++){
        if(i<confirmedTeamAppList.size()-1){
          members += (confirmedTeamAppList.get(i).getBandName() + ", ");
        }else{
          members += confirmedTeamAppList.get(i).getBandName();

        }
        
      }
      
      teamApp.setMembers(members);

      //공연성사 데이터 삽입
      teamAppMapper.insertLive(teamApp);

      String tempString = "";
      for (int i = 0; i<confirmedTeamAppList.size();i++) {
        if(i<confirmedTeamAppList.size()-1)
        tempString += confirmedTeamAppList.get(i).getPhone() + ",";
      else
        tempString += confirmedTeamAppList.get(i).getPhone();
      }
      String username = team.getUsername();
      String phonenumber = userMapper.read(username).getPhone();
      MultiValueMap<String, String> map =  new LinkedMultiValueMap<>();
      // ✅ 필수 정보
      // - receiver       :   1) 01012341234
      //                      2) 01011112222,01033334444
      // - msg            : 문자 메시지 내용
      // - testmode_yn    : 테스트 모드 여부 (Y-테스트⭕, N-테스트❌)
      // receiver에 문자 받는 사람들의 전화번호를 "," 로 연결해서 넣어주세요.
      // Title에 게시글의 제목을 입력해주세요
      // liveDate에 공연일자를 입력해주세요.
      // address에 공연장의 위치를 입력해주세요.
      String receiver = tempString + "," + phonenumber;
      String title = "『"+team.getTitle()+"』";
      String liveDate = team.getLiveDate();
      String address = team.getAddress();
      
      String msg = "LiveDom 팀 모집 서비스\n\"" + title + "\"의 공연이 성사되었습니다. \n" +
                                 "공연장 : " + address + "\n공연일자 : " + liveDate;
      String testmode_yn = "Y";
      map.add("receiver", receiver);
      map.add("msg", msg);
      map.add("testmode_yn", testmode_yn);

       log.info("메세지 발송 테스트 333 메세지 : " + msg + " 전화번호 : "+ receiver);
      Map<String, Object> resultMap = smsService.send(map);
      log.info(resultMap + "");
      Object resultCode = resultMap.get("result_code");
      Integer result_code = Integer.valueOf( resultCode != null ? resultCode.toString() : "-1" );
      String message = (String) resultMap.get("message");
      if( result_code == 1 )
          log.info("문자 발송 성공 : " + message);
      if( result_code == -1 )
          log.info("문자 발송 실패 : " + message);





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
