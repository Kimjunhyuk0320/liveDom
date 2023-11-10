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
    if(!commentList.isEmpty()){
      commentList.get(0).setTotalCount(commentMapper.totalCount(comment));
    }

    return commentList;
  }

  @Override
  public int commentInsert(Comment comment) {

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
