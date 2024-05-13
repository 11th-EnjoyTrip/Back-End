package com.travelog.attraction.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class AttractionDto {
    private Long contentId;
    private Long contentTypeId;
    private String contentTypeName;
    private String title;
    private String firstImage;
    private Long sidoCode;
    private String sidoName;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
