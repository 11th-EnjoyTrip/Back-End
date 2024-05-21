package com.travelog.attraction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttractionInfoDto {
    private int contentId;
    private int contentTypeId;
    private String title;
    private String addr1;
    private String addr2;
    private String zipcode;
    private String tel;
    private String firstImage;
    private int readCount;
    private int sidoCode;
    private int gugunCode;
    private double latitude;
    private double longitude;
}
