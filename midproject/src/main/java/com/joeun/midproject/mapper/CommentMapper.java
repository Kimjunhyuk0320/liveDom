package com.joeun.midproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joeun.midproject.dto.Comment;

@Mapper
public interface CommentMapper {

  public List<Comment> commentList(Comment comment);

  public int totalCount(Comment comment);

  public int commentInsert(Comment comment);

  public int commentDelete(Comment comment);

  public int commentUpdate(Comment comment);
  
}
