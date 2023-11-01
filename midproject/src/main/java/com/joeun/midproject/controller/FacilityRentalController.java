package com.joeun.midproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joeun.midproject.dto.FacilityRental;
import com.joeun.midproject.dto.Files;
import com.joeun.midproject.service.FacilityRentalService;
import com.joeun.midproject.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/facilityRental")
public class FacilityRentalController {
    @Autowired
    private FacilityRentalService facilityRentalService;

    @Autowired
    private FileService fileService;

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
        log.info("[GET] - /board/list");

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
     * @param facilityRentalNo
     * @param files
     * @return
     * @throws Exception
     */
    @GetMapping(value="/read")
    public String read(Model model, int facilityRentalNo, Files files) throws Exception {
        log.info("[GET] - /board/read");

        // 데이터 요청
        FacilityRental facilityRental = facilityRentalService.select(facilityRentalNo);     // 게시글 정보

        files.setParentTable("facilityRental");
        files.setParentNo(facilityRentalNo);
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
        // 데이터 처리
        int result = facilityRentalService.insert(facilityRental);

        // 게시글 쓰기 실패 ➡ 게시글 쓰기 화면
        if(result == 0) return "facilityRental/insert";

        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
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
    @GetMapping(value="/update")
    public String updatePro(FacilityRental facilityRental) throws Exception {
        // 데이터 처리
        int result = facilityRentalService.update(facilityRental);
        int facilityRentalNo = facilityRental.getFrNo();

        // 게시글 수정 실패 ➡ 게시글 수정 화면
        if(result == 0) return "redirect:/facilityRental/update?facilityRentalNo=" + facilityRentalNo;

        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
    }


    @PostMapping
    public String deletePro(int facilityRentalNo) throws Exception {
        // 데이터 처리
        int result = facilityRentalService.delete(facilityRentalNo);

        // 게시글 삭제 실패 ➡ 게시글 수정 화면
        if(result == 0) return "redirect:/facilityRental/update?facilityRentalNo=" + facilityRentalNo;

        // 뷰 페이지 지정
        return "redirect:/facilityRental/list";
    }
}
