package com.travelog.attraction.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AttractionRequestDto implements Serializable {

    @Builder.Default
    private int region=0;

    @Builder.Default
    private String category="";

    @Builder.Default
    private String keyword="";
}
