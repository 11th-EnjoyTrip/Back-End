package com.travelog.board.service;

import com.travelog.board.dto.BoardDto;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public interface BoardService {
    List<BoardDto> listArticle() throws Exception;
    List<BoardDto> searchArticle(String word) throws Exception;
    void writeArticle(BoardDto boardDto) throws Exception;
    BoardDto getArticle(int articleNo) throws Exception;
    void updateHit(int articleNo) throws Exception;
    void modifyArticle(BoardDto boardDto) throws Exception;
    void deleteArticle(int articleNo) throws Exception;
}
