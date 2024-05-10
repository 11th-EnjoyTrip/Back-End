package com.travelog.board.service;

import com.travelog.board.dao.BoardDao;
import com.travelog.board.dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService{

    private BoardDao boardDao;

    @Autowired
    private BoardServiceImpl(BoardDao boardDao) {
        this.boardDao = boardDao;
    }
    @Override
    public List<BoardDto> listArticle() throws Exception {
        return boardDao.listArticle();
    }



    @Override
    public void writeArticle(BoardDto boardDto) throws Exception {
        boardDao.writeArticle(boardDto);
    }

    @Override
    public BoardDto getArticle(int articleNo) throws Exception {
        return boardDao.getArticle(articleNo);
    }

    @Override
    public void updateHit(int articleNo) throws Exception {
        boardDao.updateHit(articleNo);
    }

    @Override
    public void modifyArticle(BoardDto boardDto) throws Exception {
        boardDao.modifyArticle(boardDto);
    }

    @Override
    public void deleteArticle(int articleNo) throws Exception {
        boardDao.deleteArticle(articleNo);
    }
}
