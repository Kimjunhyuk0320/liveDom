package com.joeun.midproject.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.Comment;
import com.joeun.midproject.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService{

  @Autowired
  private CommentMapper commentMapper;

  @Override
  public List<Comment> commentList(Comment comment) {

    List<Comment> commentList = commentMapper.commentList(comment);


    return commentList;
  }

  @Override
  public int commentInsert(Comment comment) {

    //여기서부터해라잉
    int result = commentMapper.commentInsert(comment);

    return result;
  }

  @Override
  public int commentDelete(Comment comment) {


    int result = commentMapper.commentDelete(comment);
    return result;
  }
  
  @Override
  public int commentUpdate(Comment comment) {
    
    
    int result = commentMapper.commentUpdate(comment);
    return result;
  }
  
}
