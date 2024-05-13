package com.travelog.attraction.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class AttractionRequestDto {
    private int region;
    private String category;
    private String keyword;
}
