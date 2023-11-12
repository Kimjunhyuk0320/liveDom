package com.joeun.midproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.service.FacilityRentalService;
import com.joeun.midproject.service.LiveBoardService;
import com.joeun.midproject.service.TeamService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
public class HomeController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private LiveBoardService liveBoardService;

    @Autowired
    private FacilityRentalService facilityRentalService;

    @GetMapping(value={"", "/"})
    public String home(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrf != null) {
            String token = csrf.getToken();
        } else {
        }
        return "index";
    }

    
    @GetMapping(value="/totalSearch")
  public String searchPro(Team team,Model model)  throws Exception{

    team.setSearchType(0);
    team.setOrder(0);
    team.setPageNo(0);
    team.setRows(4);
    List<LiveBoard> liveBoardList = liveBoardService.liveBoardPageList(team);
    log.info(liveBoardList.toString());
    model.addAttribute("liveList",liveBoardList);

    team.setSearchType(0);
    team.setOrder(0);
    team.setPageNo(0);
    team.setRows(4);
    List<FacilityRental> frList = facilityRentalService.pageFrList(team);
    log.info(frList.toString());
    model.addAttribute("frList", frList);

    team.setSearchType(0);
    team.setOrder(0);
    team.setPageNo(0);
    team.setRows(7);
    List<Team> teamList = teamService.pageList(team);
      model.addAttribute("teamList", teamList);
      log.info(teamList.toString());
      return "UI/user/component/team/totalSearchList";
  }

  @GetMapping(value="/moveTotalSearch")
  public String search() {
      return "totalSearch";
  }
  
    
}
