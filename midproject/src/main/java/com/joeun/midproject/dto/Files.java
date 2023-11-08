package com.joeun.midproject.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Files {
    private int fileNo;
    private String parentTable;
    private int parentNo;
    private int fileCode;
    private String path;
    private int sequence;
    private String fileName;
    private long fileSize;
    private String originName;
    private Date updDate;
    private String parentUsername;

}
