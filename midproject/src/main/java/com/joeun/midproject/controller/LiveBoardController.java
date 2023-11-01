package com.joeun.midproject.controller;

import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.service.LiveBoardService;


@Slf4j
@Controller
@RequestMapping("/liveBoard")
public class LiveBoardController {

    @Autowired
    LiveBoardService liveBoardService;

    /**
     * 공연 게시글 목록 조회
     * 
     */
    @GetMapping(value="/list")
    public String list(Model model) throws Exception{
        log.info("[GET] - /liveBoard/list");

        // 데이터 요청
        List<LiveBoard> liveBoardList = liveBoardService.list();
        // // 모델 등록
        model.addAttribute("liveBoardList", liveBoardList);
        // // 뷰 페이지 지정
        return "liveBoard/list";
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

        // 모델 등록
        model.addAttribute("liveBoard", liveBoard);

        // 뷰 페이지 지정
        return "board/read";
    }


     /**
     * 공연 게시글 쓰기
     */
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
        int result = liveBoardService.insert(liveBoard);

        // 게시글 쓰기 실패 ➡ 게시글 쓰기 화면
        if( result == 0 ) return "board/insert";

        // 뷰 페이지 지정
        return "redirect:/board/list";
    }


    /**
     * 공연 게시글 수정
     * [GET]
     */
    @GetMapping(value="/update")
    public String update(Model model, int boardNo) throws Exception {
        // 데이터 요청
        LiveBoard liveBoard = liveBoardService.select(boardNo);
        // 모델 등록
        model.addAttribute("liveBoard", liveBoard);
        // 뷰 페이지 지정
        return "board/update";
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




}
