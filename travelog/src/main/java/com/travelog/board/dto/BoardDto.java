package com.travelog.board.dto;

import lombok.Data;

@Data
public class BoardDto {
    private int id;
    private String writer;
    private String title;
    private String content;
    private int view_cnt;
    private String reg_data;
}
