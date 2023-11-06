package com.joeun.midproject.controller;

import java.security.Principal;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joeun.midproject.dto.Comment;
import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.TeamApp;
import com.joeun.midproject.mapper.TeamMapper;
import com.joeun.midproject.service.CommentService;
import com.joeun.midproject.service.TeamAppService;
import com.joeun.midproject.service.TeamService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/team")
public class TeamController {


  @Autowired
  private TeamService teamService;

  @Autowired
  private TeamAppService teamAppService;

  @Autowired
  private TeamMapper teamMapper;

  @Autowired
  private CommentService commentService;

  @GetMapping(value={"/",""})
  public String index(Model model) {

    model.addAttribute("teamList", teamService.list());

      return "team/list";
  }




  @CrossOrigin(origins = "*")
  @ResponseBody
  @GetMapping(value = "/pageInfo", produces = "application/json")
  public PageInfo pageInfo(@RequestParam("pageNo") int pageNo,
  @RequestParam("rows") int rows,
  @RequestParam("pageCount") int pageCount,
  @RequestParam("totalCount") int totalCount,
  @RequestParam("searchType") int searchType,
  @RequestParam("keyword") String keyword,
  PageInfo pageInfo){

    pageInfo.setPageNo(pageNo);
    pageInfo.setRows(rows);
    pageInfo.setPageCount(pageCount);
    pageInfo.setTotalCount(teamMapper.totalCount("team_recruitments"));
    pageInfo.setSearchType(searchType);
    pageInfo.setKeyword(keyword);

    log.info(pageInfo.toString());

    PageInfo pageInfoResult = teamService.pageInfo(pageInfo);

    log.info(pageInfoResult.toString());
    return pageInfoResult;
    
  }


  @CrossOrigin(origins = "*")
  @ResponseBody
  @GetMapping(value="/pageList", produces = "application/json")
  public List<Team> pageList(@RequestParam("pageNo") int pageNo,
  @RequestParam("rows") int rows,
  @RequestParam("searchType") int searchType,
  @RequestParam("keyword") String keyword,
  @RequestParam("order") int order,
  Team team) {

    team.setPageNo(pageNo);
    team.setRows(rows);
    team.setStartPage(pageNo);
    team.setSearchType(searchType);
    team.setKeyword(keyword);
    team.setOrder(order);

    log.info(team.toString());

    List<Team> pageListResult = teamService.pageList(team);

    for (Team team2 : pageListResult) {
      log.info(team2.toString());
    }

    return pageListResult;
  }
  




  @GetMapping(value="/insert")
  public String insert() {
      return "team/insert";
      
  }
  
  
  @Secured("ROLE_BAND")
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


      
    Team readTeam = teamService.read(team);
    
    log.info(readTeam.toString());

      model.addAttribute("team",readTeam);

      model.addAttribute("teamList", teamService.list());

      return "team/read";
  }


  @Secured("ROLE_BAND")
  @GetMapping(value="/update")
  public String update(Model model, Team team) {

      model.addAttribute("team", teamMapper.read(team));

      return "team/update";

  }

  @PostMapping(value="/update")
  public String updatePro(Team team) {

      int result = teamService.update(team);

      if(result>0){

        return "redirect:/team";
      }
      else{
        String returnName = "redirect:/team/read?teamNo="+team.getTeamNo();
        return returnName;
      }

      
      
  }
  
  
  @PostMapping(value="/delete")
  public String deletePro(Team team) {

      
      int result = teamService.delete(team);
      if(result>0){

        return "redirect:/team";
        
      }else{

        String returnName = "redirect:/team/read?teamNo="+team.getTeamNo();
        return returnName;

      }

  }

  @GetMapping(value="/app")
  public String application(Model model,Team team) {

    model.addAttribute("team",team);

      return "team/registration";
  }

  @PostMapping(value="/app")
  public String applicationPro(TeamApp teamApp) {

    int result = teamAppService.insert(teamApp);
      
      return "redirect:/team/user/listByMember";
    }
    
    @PostMapping(value="/app/delete")
    public String appDeletePro(TeamApp teamApp) {
      
      int result = teamAppService.delete(teamApp);

      log.info(result+"");
      return "redirect:/team/user/listByMember";
 }
 

  @PostMapping(value="/app/accept")
  public String acceptPro(TeamApp teamApp) {
   
    int result = teamAppService.accept(teamApp);
      
    return "redirect:/team/user/listByLeader";
  }
  
  
  @PostMapping(value="/app/denied")
  public String deniedPro(TeamApp teamApp) {
   
      int result = teamAppService.denied(teamApp);

      //페이지 갱신이 필요합니다.
      //갱신된 리스트를 다시 조회하여 반환해야합니다.
      //추후 비동기 방식으로 수정 예정입니다.

      return "redirect:/team/user/listByLeader";
  }

  @PostMapping(value="/app/confirmed")
  public String confirmedPro(TeamApp teamApp) {
   
      int result = teamAppService.confirmed(teamApp);

      //페이지 갱신이 필요합니다.
      //갱신된 리스트를 다시 조회하여 반환해야합니다.
      //추후 비동기 방식으로 수정 예정입니다.

    return "redirect:/team/user/listByLeader";

  }

  @GetMapping(value="/user/listByLeader")
  public String listByLider(Model model, TeamApp teamApp,Principal principal) {

    teamApp.setUsername(principal.getName());
    model.addAttribute("resTeamAppList", teamAppService.listByLeader(teamApp));
    
    return "myPage/myPageForBand/team_registrations_list";
    
  }
  
  
  

  @GetMapping(value="/user/listByMember")
  public String listByMember(Model model, TeamApp teamApp, Principal principal) {
    log.info(principal.getName());
    teamApp.setUsername(principal.getName());
    List<TeamApp> appList =  teamAppService.listByMember(teamApp);
    log.info(appList.toString());
    model.addAttribute("reqTeamAppList",appList);

      return "myPage/myPageForBand/my_registration_list";

  }

  @GetMapping(value="/user/listByConfirmedLive")
  public String listByConfirmedLive() {
    
    return "myPage/myPageForBand/completed_performances_list";
  }
  
  @CrossOrigin(origins = "*")
  @ResponseBody
  @GetMapping(value = "/pageInfoConfirmedLive", produces = "application/json")
  public PageInfo pageInfoConfirmedLIve(@RequestParam("pageNo") int pageNo,
  @RequestParam("rows") int rows,
  @RequestParam("pageCount") int pageCount,
  @RequestParam("totalCount") int totalCount,
  @RequestParam("searchType") int searchType,
  @RequestParam("keyword") String keyword,
  PageInfo pageInfo){

    pageInfo.setPageNo(pageNo);
    pageInfo.setRows(rows);
    pageInfo.setPageCount(pageCount);
    pageInfo.setTotalCount(teamMapper.totalCount("confirmed_live"));
    pageInfo.setSearchType(searchType);
    pageInfo.setKeyword(keyword);

    log.info(pageInfo.toString());

    PageInfo pageInfoResult = teamService.pageInfo(pageInfo);

    log.info(pageInfoResult.toString());
    return pageInfoResult;
    
  }

  @CrossOrigin(origins = "*")
  @ResponseBody
  @GetMapping(value="/confirmedLiveList", produces = "application/json")
  public List<Team> confirmedLiveList(Team team, Principal principal) {
    
    log.info("공연성사 요청값"+(team.toString()));
    
    List<Team> pageListResult = teamService.listByConfirmedLive2(team);
    
    for (Team team2 : pageListResult) {
      log.info("공연성사 리스트 : "+team2.toString());
    }
    
    return pageListResult;
  }

  @GetMapping(value="/user/readApp")
  public String readApp(Model model, TeamApp teamApp) {

    TeamApp readApp = teamAppService.read(teamApp);

    model.addAttribute("teamApp", readApp);

      return "myPage/myPageForBand/team_registrations_read";
  }


  @ResponseBody
  @CrossOrigin(origins="*")
  @GetMapping(value="/commentList", produces = "application/json")
  public List<Comment> commentList(Comment comment) {
    comment.setParentTable("team_recruitments");
    log.info(comment.toString());
    List<Comment> commentList = commentService.commentList(comment);

    for (Comment comment2 : commentList) {
      log.info(comment2.toString());
    }
    return commentList;
  }

  @ResponseBody
  @CrossOrigin(origins="*")
  @GetMapping(value="/commentInsert")
  public String commentInsert(Comment comment) {

    comment.setParentTable("team_recruitments");
    int result = commentService.commentInsert(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }

  @ResponseBody
  @CrossOrigin(origins="*")
  @GetMapping(value="/commentDelete")
  public String commentDelete(Comment comment) {

    comment.setParentTable("team_recruitments");
    int result = commentService.commentDelete(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }

  @ResponseBody
  @CrossOrigin(origins="*")
  @GetMapping(value="/commentUpdate")
  public String commentUpdate(Comment comment) {

    comment.setParentTable("team_recruitments");
    int result = commentService.commentUpdate(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }
  
  
  
  
  
  
  
}
