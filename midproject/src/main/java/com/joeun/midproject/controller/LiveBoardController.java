package com.joeun.midproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joeun.midproject.dto.Files;
import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.mapper.FileMapper;
import com.joeun.midproject.mapper.TeamMapper;
import com.joeun.midproject.service.LiveBoardService;


@Slf4j
@Controller
@RequestMapping("/liveBoard")
public class LiveBoardController {

    @Autowired
    LiveBoardService liveBoardService;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    private TeamMapper teamMapper;

    /**
     * 공연 게시글 목록 조회
     * 
     */
    @GetMapping(value={"/list",""})
    public String list(Model model) throws Exception{
        log.info("[GET] - /liveBoard/list");

        // 데이터 요청
        List<LiveBoard> liveBoardList = liveBoardService.list();
        // // 모델 등록
        model.addAttribute("liveBoardList", liveBoardList);
        // // 뷰 페이지 지정
        return "liveBoard/list";
    }

@CrossOrigin(origins = "*")
  @ResponseBody
  @GetMapping(value = "/pageInfoLiveBoard", produces = "application/json")
  public PageInfo pageInfoLiveBoard(PageInfo pageInfo){

    pageInfo.setTotalCount(teamMapper.totalCount("live_board"));

    log.info(pageInfo.toString());

    PageInfo pageInfoResult = teamMapper.pageInfo(pageInfo);

    log.info(pageInfoResult.toString());
    return pageInfoResult;
    
  }

  @CrossOrigin(origins = "*")
  @ResponseBody
  @GetMapping(value="/liveBoardPageList", produces = "application/json")
  public List<LiveBoard> liveBoardPageList(Team team) throws Exception{
    
    log.info("공연성사 요청값"+(team.toString()));
    
    List<LiveBoard> pageListResult = liveBoardService.liveBoardPageList(team);
    
    for (LiveBoard team2 : pageListResult) {
      log.info("공연성사 리스트 : "+team2.toString());
    }
    
    return pageListResult;
  }
    

    /**
     * 공연 게시글 조회
     * 
     */
    @GetMapping(value="/read")
    public String read(Model model, int boardNo) throws Exception {
        log.info("[GET] - /liveBoard/read");

        // 데이터 요청
        LiveBoard liveBoard = liveBoardService.select(boardNo);     // 게시글 정보
        int totalTicketCount = liveBoard.getMaxTickets();
        List<Ticket> ticketList = liveBoardService.listByBoardNo(boardNo);
        int soldTicketCount = ticketList.size();
        int nowTicketCount = totalTicketCount - soldTicketCount;
        liveBoard.setTicketLeft(nowTicketCount);
 
        // 모델 등록
        model.addAttribute("liveBoard", liveBoard);

        // 뷰 페이지 지정
        return "liveBoard/read";
    }


     /**
     * 공연 게시글 쓰기
     */
    @Secured({"ROLE_BAND", "ROLE_CLUB"})
    @GetMapping(value="/insert")
    public String insert() {
        return "liveBoard/insert";
    }


     /**
     * 공연 게시글 쓰기 처리
     * [POST]
     */
    
    @PostMapping(value="/insert")
    public String insertPro(@ModelAttribute LiveBoard liveBoard) throws Exception {
        // @ModelAttribute : 모델에 자동으로 등록해주는 어노테이션
        // 데이터 처리
        String liveStTime = liveBoard.getLiveStTime();
        String liveEndTime = liveBoard.getLiveEndTime();
        String liveTime = liveStTime + " ~ " + liveEndTime;
        liveBoard.setLiveTime(liveTime);
        log.info(liveBoard + "");
        int result = liveBoardService.insert(liveBoard);

        // 게시글 쓰기 실패 ➡ 게시글 쓰기 화면
        if( result == 0 ) return "liveBoard/insert";

        // 뷰 페이지 지정
        return "redirect:/liveBoard/list";
    }


    /**
     * 공연 게시글 수정
     * [GET]
     */
    @Secured({"ROLE_BAND", "ROLE_CLUB"})
    @GetMapping(value="/update")
    public String update(Model model, int boardNo) throws Exception {
        // 데이터 요청
        LiveBoard liveBoard = liveBoardService.select(boardNo);
        // 모델 등록
        model.addAttribute("liveBoard", liveBoard);
        // 뷰 페이지 지정
        return "liveBoard/update";
    }


     /**
     * 공연 게시글 수정 처리
     * [POST]
     */
    @PostMapping(value="/update")
    public String updatePro(LiveBoard liveBoard) throws Exception {
        // 데이터 처리
        int result = liveBoardService.update(liveBoard);
        int boardNo = liveBoard.getBoardNo();

        // 게시글 수정 실패 ➡ 게시글 수정 화면
        if( result == 0 ) return "redirect:/liveBoard/update?boardNo=" + boardNo;
        
        // 뷰 페이지 지정
        return "redirect:/liveBoard/list";
    }
     /**
     * 티켓 구매 처리
     * [POST]
     */
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_BAND", "ROLE_CLUB"})
    @PostMapping(value="/purchase")
    public String ticket(Ticket ticket, int count) throws Exception {
        log.info("ajax 티켓 구매 처리 테스트");
        int boardNo = ticket.getBoardNo();
        int totalTicketCount = liveBoardService.select(boardNo).getMaxTickets();
        List<Ticket> ticketList = liveBoardService.listByBoardNo(boardNo);
        int purchaseTicketCount = ticketList.size();
        int ticketLeft = totalTicketCount - purchaseTicketCount;
        // 잔여티켓보다 구매티켓이 많은경우의 응답
        if( ticketLeft < count) return "OVERCOUNT";

        // 잔여티켓의 수가 0 일때 매진 응답
        if( (Integer)ticketLeft == 0 ) return "ZERO";


        // 데이터 처리
        int result = 0;
        for(int i = 0 ; i < count ; i++){
            result += liveBoardService.purchase(ticket);
        }

        // 티켓 구매 실패 응답
        if( result == 0 ) return "FAIL";

        //잔여티켓수 0 일시 매진으로 변환
        ticketList = liveBoardService.listByBoardNo(boardNo);
        int afterTicketCount = ticketList.size();
        int afterCount = totalTicketCount - afterTicketCount;
        if((Integer)afterCount == 0 ){
            int update = liveBoardService.soldOut(boardNo);
            log.info( "잔여 티켓수 0 일시 매진으로 전환 : " + update);
        }
        // 성공응답
        return "SUCCESS";
    }

    /**
     * 티켓 구매 완료 페이지
     * [GET]
     */
    @GetMapping(value="/complete")
    public String complete() throws Exception {
       
        return "liveBoard/complete";
    }


}
