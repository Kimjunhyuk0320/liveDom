package com.joeun.midproject.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joeun.midproject.dto.BookingRequests;
import com.joeun.midproject.dto.Comment;
import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.Files;
import com.joeun.midproject.dto.PageInfo;
import com.joeun.midproject.dto.Team;
import com.joeun.midproject.mapper.TeamMapper;
import com.joeun.midproject.service.CommentService;
import com.joeun.midproject.service.FacilityRentalService;
import com.joeun.midproject.service.FileService;
import com.joeun.midproject.service.TeamService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;




@Slf4j
@Controller
@RequestMapping("/facilityRental")
public class FacilityRentalController {
    @Autowired
    private FacilityRentalService facilityRentalService;
    
    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamService teamService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommentService commentService;

    /**
     * 게시글 목록
     * [GET]
     * /facilityList
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value="/list")
    public String list(Model model) throws Exception {
        log.info("[GET] - /facilityRental/list");

        // 데이터 요청
        List<FacilityRental> facilityList = facilityRentalService.list();
        // 모델 등록
        model.addAttribute("facilityList", facilityList);
        // 뷰 페이지 지정
        return "facilityRental/list";
    }


    /**
     * 게시글 조회
     * [GET]
     * /facilityRental/read
     * - model : facilityRental, fileList
     * @param model
     * @param frNo
     * @param files
     * @return
     * @throws Exception
     */
    @GetMapping(value="/read")
    public String read(Model model, int frNo, Files files) throws Exception {
        log.info("[GET] - /facilityRental/read");

        // 데이터 요청
        FacilityRental facilityRental = facilityRentalService.select(frNo);     // 게시글 정보

        files.setParentTable("facilityRental");
        files.setParentNo(frNo);
        List<Files> fileList = fileService.listByParent(files); // 파일 정보

        // 모델 등록
        model.addAttribute("facilityRental", facilityRental);
        model.addAttribute("fileList", fileList);
        // 뷰 페이지 지정
        return "facilityRental/read";
    }


    /**
     * 게시글 쓰기
     * [GET]
     * /facilityRental/insert
     * model : ❌
     * @param facilityRental
     * @return
     */
    @GetMapping(value="/insert")
    public String insert(@ModelAttribute FacilityRental facilityRental) {
        return "facilityRental/insert";
    }


    /**
     * 게시글 쓰기 처리
     * [POST]
     * /facilityRental/insert
     * model : ❌
     * @param facilityRental
     * @return
     * @throws Exception
     */
    @PostMapping(value="/insert")
    public String insertPro(@ModelAttribute FacilityRental facilityRental) throws Exception {
        // @ModelAttribute : 모델에 자동으로 등록해주는 어노테이션
        facilityRental.setAccount(facilityRental.getAccount1()+"/"+facilityRental.getAccount2());
        // 데이터 처리
        int result = facilityRentalService.insert(facilityRental);

        // 게시글 쓰기 실패 ➡ 게시글 쓰기 화면
        if(result == 0) return "facilityRental/insert";

        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
    }


     /**
     * 게시글 수정
     * [GET]
     * /facilityRental/update
     * model : facilityRental
     * @param frNo
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value="/update")
    public String update(Model model, int frNo) throws Exception {
        // 데이터 요청
        FacilityRental facilityRental = facilityRentalService.select(frNo);
        facilityRental.setAccount1(facilityRental.getAccount().split("/")[0]);
        facilityRental.setAccount2(facilityRental.getAccount().split("/")[1]);
        // 모델 등록
        model.addAttribute("facilityRental", facilityRental);
        // 뷰 페이지 지정
        return "facilityRental/update";
    }


    /**
     * 게시글 수정 처리
     * [POST]
     * /facilityRental/update
     * model : facilityRental
     * @param facilityRental
     * @return
     * @throws Exception
     */
    @PostMapping(value="/update")
    public String updatePro(FacilityRental facilityRental) throws Exception {
      // 데이터 처리
      log.info(facilityRental.toString());
      facilityRental.setAccount(facilityRental.getAccount1()+"/"+facilityRental.getAccount2());
        int result = facilityRentalService.update(facilityRental);
        int frNo = facilityRental.getFrNo();

        // 게시글 수정 실패 ➡ 게시글 수정 화면
        if(result == 0) return "redirect:/facilityRental/update?frNo=" + frNo;

        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
    }


    /**
     * 게시글 삭제 처리
     * [POST]
     * /facilityRental/delete
     * model : ❌
     * @param frNo
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/delete")
    public String deletePro(int frNo) throws Exception {
        // 데이터 처리
        int result = facilityRentalService.delete(frNo);

        // 게시글 삭제 실패 ➡ 게시글 수정 화면
        if(result == 0) return "redirect:/facilityRental/update?frNo=" + frNo;

        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
    }


    /**
     * 대관 예약 신청
     * [POST]
     * /facilityRental/read
     * @param bookingRequests
     * @param count
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/reservation")
    public String reservation(@ModelAttribute BookingRequests bookingRequests) throws Exception {
        log.info("메시지");
        // 데이터 처리
        int result = facilityRentalService.reservation(bookingRequests);
        // 게시글 쓰기 실패 ➡ 게시글 쓰기 화면
        if(result == 0) return "facilityRental/list";
        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
    }

    @GetMapping(value = "/complete")
    public String complete() throws Exception {
        return "/";
    }

    @GetMapping(value="/user/receivedList")
    public String receivedList(Model model,Principal principal) throws Exception{

        List<BookingRequests> rrList = facilityRentalService.rrList(principal.getName());
        log.info(rrList.toString());
        model.addAttribute("rrList", rrList);

        return "myPage/received_rental_list";
    }

    @GetMapping(value="/user/reqList")
    public String reqList(Model model,Principal principal) throws Exception{

        List<BookingRequests> rreqList = facilityRentalService.rreqList(principal.getName());

        model.addAttribute("rreqList", rreqList);

        return "myPage/rental_requests_list";
    }
    

    @PostMapping(value="/reqDel")
    public String reqDelPro(BookingRequests bookingRequests) throws Exception{

        int result = facilityRentalService.delReq(bookingRequests);
        
        return "redirect:/facilityRental/user/reqList";
    }
    
    @PostMapping(value="/reqDenied")
    public String reqDenied(BookingRequests bookingRequests) throws Exception{

        int result = facilityRentalService.reqDenied(bookingRequests);
        
        return "redirect:/user/receivedList";
    }
    
    @PostMapping(value="/reqAccept")
    public String reqAccept(BookingRequests bookingRequests) throws Exception{

        int result = facilityRentalService.reqAccept(bookingRequests);

        return "redirect:/facilityRental/user/receivedList";
    }
    
    @PostMapping(value="/reqConfirm")
    public String reqConfirm(BookingRequests bookingRequests) throws Exception {
        
        int result = facilityRentalService.reqConfirm(bookingRequests);

        return "redirect:/facilityRental/user/receivedList";
    }


  @ResponseBody
  @GetMapping(value = "/pageInfoFr", produces = "application/json")
  public PageInfo pageInfoFr(PageInfo pageInfo){

    pageInfo.setTable("facility_rental");
    pageInfo.setTotalCount(teamMapper.totalCount(pageInfo));

    log.info(pageInfo.toString());

    PageInfo pageInfoResult = teamService.pageInfo(pageInfo);

    log.info(pageInfoResult.toString());
    return pageInfoResult;
    
  }

  @ResponseBody
  @GetMapping(value="/pageFrList", produces = "application/json")
  public List<FacilityRental> confirmedLiveList(Team team, Principal principal)throws Exception{
    
    List<FacilityRental> pageListResult = facilityRentalService.pageFrList(team);
    
    for (FacilityRental team2 : pageListResult) {
      log.info("대관글 리스트 : "+team2.toString());
    }
    
    return pageListResult;
  }
    




  
  @ResponseBody
  @GetMapping(value="/commentList", produces = "application/json")
  public List<Comment> commentList(Comment comment) {
    comment.setParentTable("facility_rental");
    log.info(comment.toString());
    List<Comment> commentList = commentService.commentList(comment);

    for (Comment comment2 : commentList) {
      log.info(comment2.toString());
    }
    return commentList;
  }

  @ResponseBody
  @GetMapping(value="/commentInsert")
  public String commentInsert(Comment comment) {

    comment.setParentTable("facility_rental");
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

    comment.setParentTable("facility_rental");
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

    comment.setParentTable("facility_rental");
    int result = commentService.commentUpdate(comment);
    if(result>0){
      return "SUCCESS";
    }
    else{

      return "FAILED";
    }
  }
    
    
    
    
}
