package com.joeun.midproject.service;

import java.util.List;

import com.joeun.midproject.dto.Comment;


public interface CommentService {
  
  public List<Comment> commentList(Comment comment);

  public int commentInsert(Comment comment);

  public int commentDelete(Comment comment);

  public int commentUpdate(Comment comment);

}
