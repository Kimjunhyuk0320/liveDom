package com.joeun.midproject.controller;

import java.security.Principal;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.TeamApp;
import com.joeun.midproject.mapper.TeamMapper;
import com.joeun.midproject.service.TeamAppService;
import com.joeun.midproject.service.TeamService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/team")
public class TeamController {


  @Autowired
  private TeamService teamService;

  @Autowired
  private TeamAppService teamAppService;

  @Autowired
  private TeamMapper teamMapper;



  @GetMapping(value={"/",""})
  public String index(Model model) {

    model.addAttribute("teamList", teamService.list());

      return "team/list";
  }



  @GetMapping(value="/insert")
  public String insert() {
      return "team/insert";
      
  }
  
  

  @PostMapping(value="/insert")
  public String insertPro(HttpServletRequest request,Team team) {
    
// Enumeration<String> parameterNames = request.getParameterNames();
//         while (parameterNames.hasMoreElements()) {
//             String paramName = parameterNames.nextElement();
//             String[] paramValues = request.getParameterValues(paramName);
//             System.out.print(paramName + ": ");
//             for (String paramValue : paramValues) {
//                 System.out.print(paramValue + " ");
//             }
//             System.out.println();
//         }

    int result = teamService.insert(team);

    if(result>0){
      return "team/list";
    }else{
      return "team/insert";

    }

  }

  @GetMapping(value="/read")
  public String read(Model model, Team team) {


      
      model.addAttribute("team", teamService.read(team));
      model.addAttribute("teamList", teamService.list());

      return "team/read";
  }



  @GetMapping(value="/update")
  public String update(Model model, Team team) {

      model.addAttribute("team", teamMapper.read(team));

      return "team/update";

  }

  @PostMapping(value="/update")
  public String updatePro(Team team) {

      int result = teamService.update(team);

      if(result>0){

        return "team/list";
      }
      else{
        return "team/update";
      }

      
      
  }
  
  
  @PostMapping(value="delete")
  public String deletePro(Team team) {

      
      int result = teamService.delete(team);
      if(result>0){

        return "team/list";
      }else{
        return "team/list";

      }
  }

  @GetMapping(value="/app")
  public String application(Model model,Team team) {

    model.addAttribute("team",team);

      return "team/teamApp";
  }

  @PostMapping(value="/app")
  public String applicationPro(TeamApp teamApp) {

    int result = teamAppService.insert(teamApp);
      
      return "myPage/myPageForBand/my_registration_list";
  }

  @PostMapping(value="/app/accept")
  public String acceptPro(TeamApp teamApp) {
   
    int result = teamAppService.accept(teamApp);
      
    return "myPage/myPageForBand/team_registrations_list";
  }
  
  
  @PostMapping(value="/app/denied")
  public String deniedPro(TeamApp teamApp) {
   
      int result = teamAppService.denied(teamApp);

      //페이지 갱신이 필요합니다.
      //갱신된 리스트를 다시 조회하여 반환해야합니다.
      //추후 비동기 방식으로 수정 예정입니다.

      return "myPage/myPageForBand/team_registrations_list";
  }

  @PostMapping(value="/app/confirmed")
  public String confirmedPro(TeamApp teamApp) {
   
      int result = teamAppService.confirmed(teamApp);

      //페이지 갱신이 필요합니다.
      //갱신된 리스트를 다시 조회하여 반환해야합니다.
      //추후 비동기 방식으로 수정 예정입니다.

    return "myPage/myPageForBand/team_registrations_list";

  }

  @GetMapping(value="/user/listByLeader")
  public String listByLider(Model model, TeamApp teamApp,Principal principal) {

    teamApp.setUsername(principal.getName());
    model.addAttribute("resTeamAppList", teamAppService.listByLeader(teamApp));

      return "myPage/myPageForBand/team_registrations_list";
      
  }


  @GetMapping(value="/user/listByMember")
  public String listByMember(Model model, TeamApp teamApp, Principal principal) {
    teamApp.setUsername(principal.getName());
    model.addAttribute("resTeamAppList", teamAppService.listByMember(teamApp));

      return "myPage/myPageForBand/my_registration_list";

  }

  @GetMapping(value="/user/listByConfirmedLive")
  public String listByConfirmedLive(Principal principal,Model model) {

    String username = principal.getName();

    model.addAttribute("confirmedTeamList", teamService.listByConfirmedLive(username));

      return "myPage/myPageForBand/completed_performances_list";
  }
  
  
  
  
  
  
}
