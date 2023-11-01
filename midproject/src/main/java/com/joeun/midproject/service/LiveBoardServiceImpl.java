package com.joeun.midproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.mapper.LiveBoardMapper;

@Service
public class LiveBoardServiceImpl implements LiveBoardService{
    @Autowired
    LiveBoardMapper liveBoardMapper;

    @Override
    public int insert(LiveBoard liveBoard) throws Exception {
        int result = liveBoardMapper.insert(liveBoard);
        return result;
    }

    @Override
    public int update(LiveBoard liveBoard) throws Exception {
        int result = liveBoardMapper.update(liveBoard);
        return result;
    }

    @Override
    public LiveBoard select(int boardNo) throws Exception {
        LiveBoard liveBoard = liveBoardMapper.select(boardNo);
        return liveBoard;
    }

    @Override
    public List<LiveBoard> list() throws Exception {
        List<LiveBoard> liveBoardList = liveBoardMapper.list();
        return liveBoardList;
    }

    @Override
    public int soldOut(int boardNo) {
        int result = liveBoardMapper.soldOut(boardNo);
        return result;
    }
    
}
