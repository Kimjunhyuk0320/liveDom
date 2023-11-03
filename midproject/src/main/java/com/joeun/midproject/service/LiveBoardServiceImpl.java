package com.joeun.midproject.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeun.midproject.dto.LiveBoard;
import com.joeun.midproject.dto.Ticket;
import com.joeun.midproject.dto.Users;
import com.joeun.midproject.mapper.LiveBoardMapper;
import com.joeun.midproject.mapper.TicketMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LiveBoardServiceImpl implements LiveBoardService{
    @Autowired
    LiveBoardMapper liveBoardMapper;

    @Autowired
    TicketMapper ticketMapper;

    @Override
    public int insert(LiveBoard liveBoard) throws Exception {
        int result = liveBoardMapper.insert(liveBoard);
        return result;
    }

    @Override
    public int update(LiveBoard liveBoard) throws Exception {
        int result = liveBoardMapper.update(liveBoard);
        log.info(result + "");
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
    public int soldOut(int boardNo) throws Exception{
        int result = liveBoardMapper.soldOut(boardNo);
        return result;
    }

    @Override
    public int purchase(Ticket ticket) throws Exception {
        String reservationUuid = UUID.randomUUID().toString();
        String reservationNo = "T" + reservationUuid;
        ticket.setReservationNo(reservationNo);
        int result = ticketMapper.insert(ticket);
        return result;
    }

    @Override
    public List<Ticket> listByBoardNo(int boardNo) throws Exception {
       List<Ticket> ticketList = ticketMapper.listByBoardNo(boardNo);
       return ticketList;
    }

    
    
}
