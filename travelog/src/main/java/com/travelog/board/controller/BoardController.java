package com.travelog.board.controller;

import com.travelog.board.dto.BoardDto;
import com.travelog.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "board")
@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "refreshToken", "Content-Type"})
public class BoardController {
    private static final String SUCCESS = "success";
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    @Transactional
    public ResponseEntity<?> list() {
        try {
            List<BoardDto> list = boardService.listArticle();
            if (list != null && !list.isEmpty()) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }
    @GetMapping("/view/{articleno}")
    @Transactional
    public ResponseEntity<?> write(@PathVariable("articleno") int articleNo) {
        try {
            BoardDto boardDto = boardService.getArticle(articleNo);
            if(boardDto != null) {
                boardService.updateHit(articleNo);
                return new ResponseEntity<>(boardDto, HttpStatus.OK);
            }else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }
    @GetMapping("/search/{word}")
    @Transactional
    public ResponseEntity<?> search(@PathVariable("word") String word){
        try{
            List<BoardDto> list = boardService.searchArticle(word);
            if(list != null && !list.isEmpty()){
                return new ResponseEntity<>(list, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }catch(Exception e){
            return exceptionHandling(e);
        }
    }
    @PostMapping("/write")
    @Transactional
    public ResponseEntity<?> write(@RequestBody BoardDto boardDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            boardService.writeArticle(boardDto);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }



    @PutMapping("/{articleno}")
    @Transactional
    public ResponseEntity<?> modify(@PathVariable("articleno") int articleNo, @RequestBody BoardDto boardDto) {
        boardDto.setId(articleNo);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            boardService.modifyArticle(boardDto);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @DeleteMapping("/{articleno}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("articleno") int articleNo) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        try {
            boardService.deleteArticle(articleNo);
            resultMap.put("message", SUCCESS);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }


    private ResponseEntity<String> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
