package com.joeun.midproject.dto;

import lombok.Data;

@Data
public class PageInfo {
    
    private int totalCount;
    private int pageNo;
    private int pageCount;
    private int rows;
    private int searchType;
    private String keyword;
    private int lastPage;
    private int startPage;
    private int endPage;
    private int firstPage;
    private int next;
    private int prev;
    private String table;
    
}