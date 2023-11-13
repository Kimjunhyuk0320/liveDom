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

import com.joeun.midproject.dto.Comment;
import com.joeun.midproject.dto.Files;
import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.mapper.FileMapper;
import com.joeun.midproject.mapper.TeamMapper;
import com.joeun.midproject.service.CommentService;
import com.joeun.midproject.service.LiveBoardService;
import org.springframework.web.bind.annotation.RequestBody;



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

    @Autowired
    private CommentService commentService;

    

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

  @ResponseBody
  @GetMapping(value = "/pageInfoLiveBoard", produces = "application/json")
  public PageInfo pageInfoLiveBoard(PageInfo pageInfo){
    pageInfo.setTable("live_board");
    pageInfo.setTotalCount(teamMapper.totalCount(pageInfo));


    PageInfo pageInfoResult = teamMapper.pageInfo(pageInfo);

    return pageInfoResult;
    
  }

  @ResponseBody
  @GetMapping(value="/liveBoardPageList", produces = "application/json")
  public List<LiveBoard> liveBoardPageList(Team team) throws Exception{
    
    
    List<LiveBoard> pageListResult = liveBoardService.liveBoardPageList(team);
    
    
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
      
      @PostMapping(value="/delete")
      public String deletePro(LiveBoard liveBoard,Model model) throws Exception{
     
        int result = liveBoardService.delete(liveBoard);
        if(result>0){
          return "redirect:/liveBoard/list";
          
        }else{
           // 데이터 요청
           int boardNo = liveBoard.getBoardNo();
        LiveBoard tempLiveBoard = liveBoardService.select(boardNo);     // 게시글 정보
        int totalTicketCount = liveBoard.getMaxTickets();
        List<Ticket> ticketList = liveBoardService.listByBoardNo(boardNo);
        int soldTicketCount = ticketList.size();
        int nowTicketCount = totalTicketCount - soldTicketCount;
        liveBoard.setTicketLeft(nowTicketCount);
 
        // 모델 등록
        model.addAttribute("liveBoard", tempLiveBoard);
          return "redirect:/liveBoard/read";
        }

        
    }
    

     /**
     * 티켓 수량 비동기 조회
     * [POST]
     */
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_BAND", "ROLE_CLUB"})
    @PostMapping(value="/ticketNum")
    public String ticketNum(Ticket ticket, int count) throws Exception {
        int boardNo = ticket.getBoardNo();
        int totalTicketCount = liveBoardService.select(boardNo).getMaxTickets();
        List<Ticket> ticketList = liveBoardService.listByBoardNo(boardNo);
        int purchaseTicketCount = ticketList.size();
        int ticketLeft = totalTicketCount - purchaseTicketCount;
        // 티켓 수량이 0개 일때 응답
        if( count == 0) return "TICKETZERO";
        // 잔여티켓보다 구매티켓이 많은경우의 응답
        if( ticketLeft < count) return "OVERCOUNT";

        // 잔여티켓의 수가 0 일때 매진 응답
        if( (Integer)ticketLeft == 0 ) return "ZERO";

      return "SUCCESS";
    }
    
     /**
     * 티켓 구매 처리
     * [POST]
     */
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_BAND", "ROLE_CLUB"})
    @PostMapping(value="/purchase")
    public String ticket(Ticket ticket, int count) throws Exception {
        int boardNo = ticket.getBoardNo();
        int totalTicketCount = liveBoardService.select(boardNo).getMaxTickets();
        List<Ticket> ticketList = liveBoardService.listByBoardNo(boardNo);
        int purchaseTicketCount = ticketList.size();
        int ticketLeft = totalTicketCount - purchaseTicketCount;
        if( count == 0) return "TICKETZERO";
        // 잔여티켓보다 구매티켓이 많은경우의 응답
        if( ticketLeft < count) return "OVERCOUNT";

        // 잔여티켓의 수가 0 일때 매진 응답
        if( (Integer)ticketLeft == 0 ) return "ZERO";


        // 티켓 테이블에 등록
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






    
  @ResponseBody
  @GetMapping(value="/commentList", produces = "application/json")
  public List<Comment> commentList(Comment comment) {
    comment.setParentTable("live_board");
    List<Comment> commentList = commentService.commentList(comment);

    return commentList;
  }

  @ResponseBody
  @GetMapping(value="/commentInsert")
  public String commentInsert(Comment comment) {

    comment.setParentTable("live_board");
    int result = commentService.commentInsert(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }

  @ResponseBody
  @GetMapping(value="/commentDelete")
  public String commentDelete(Comment comment) {

    comment.setParentTable("live_board");
    int result = commentService.commentDelete(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }

  @ResponseBody
  @GetMapping(value="/commentUpdate")
  public String commentUpdate(Comment comment) {

    comment.setParentTable("live_board");
    int result = commentService.commentUpdate(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }


}
