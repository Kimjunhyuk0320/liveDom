package com.joeun.midproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.joeun.midproject.dto.Files;
import com.joeun.midproject.service.FileService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;


    /**
     * 파일 다운로드
     * @param fileNo
     * @param response
     * @throws Exception
     */
    @GetMapping(value="/{fileNo}")
    public void fileDownload(@PathVariable("fileNo") int fileNo
                             ,HttpServletResponse response) throws Exception {

        int result = fileService.download(fileNo, response);

        if(result == 0) {
            response.setStatus(response.SC_BAD_REQUEST);
        }
    }

    @GetMapping(value="/img/{fileNo}")
    public void thumbnail(@PathVariable("fileNo") int fileNo, 
                                HttpServletResponse response ) throws Exception{
        int result = fileService.thumbnail(fileNo, response);
        if( result == 0 ){
            response.setStatus(response.SC_BAD_REQUEST);
        }                                
    }

    @PostMapping(value="")
    public ResponseEntity<Integer> uploadImg(List<MultipartFile> file) throws Exception {
        log.info("#########테스트입니다 1111############");
        Integer fileNo = fileService.uploadImg(file);
        log.info("컨트롤러 응답 반환 파일 번호 : " + fileNo);
         return new ResponseEntity<Integer>(fileNo, HttpStatus.OK);
    }
    


    /**
     * 파일 삭제
     * @param file
     * @return
     * @throws Exception
     */
    @DeleteMapping("")
    public ResponseEntity<String> deleteFile(Files file) throws Exception {
        log.info("[DELETE] - /file");
        int fileNo = file.getFileNo();
        log.info("fileNo : " + fileNo);
        if(fileNo == 0)
            return new ResponseEntity<String>("FAIL", HttpStatus.BAD_REQUEST);

        int result = fileService.delete(fileNo);

        if(result == 0)
            return new ResponseEntity<String>("FAIL", HttpStatus.OK);
        
        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }
}
