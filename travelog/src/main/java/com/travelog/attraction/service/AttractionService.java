package com.travelog.attraction.service;

import com.travelog.attraction.dto.AttractionDetailDto;
import com.travelog.attraction.dto.AttractionRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface AttractionService {
    Page<Map<String, Object>> getAttractionList(AttractionRequestDto attractionRequestDto, Pageable pageable) throws Exception;
    AttractionDetailDto getAttractionDetail(int id) throws Exception;
}
