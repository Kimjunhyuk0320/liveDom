package com.joeun.midproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.TeamApp;
import com.joeun.midproject.mapper.TeamMapper;
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
  public String insertPro(Team team) {
    
    int result = teamService.insert(team);

    if(result>0){
      return "team/insert";
    }

      return "team/list";
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

    
      
      return "user/mypage/reqApp";
  }

  @PostMapping(value="/app/accept")
  public void acceptPro(TeamApp teamApp) {
   
      

  }
  
  
  
  
  
  
  
}
