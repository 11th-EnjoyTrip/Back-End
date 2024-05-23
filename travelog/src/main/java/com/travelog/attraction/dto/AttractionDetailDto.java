package com.travelog.attraction.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;


@Data
public class AttractionDetailDto {
    private Long contentId;
    private Long contentTypeId;
    private String contentTypeName;
    private String title;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String tel;
    private Boolean isLikedAttraction;
    private String firstImage;
    private String firstImage2;
    private int likes;
    private Long sidoCode;
    private String sidoName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String overview;
}
