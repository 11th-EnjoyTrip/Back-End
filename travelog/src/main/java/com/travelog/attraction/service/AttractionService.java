package com.travelog.attraction.service;

import com.travelog.attraction.dto.AttractionDetailDto;
import com.travelog.attraction.dto.AttractionInfoDto;
import com.travelog.attraction.dto.AttractionRequestDto;
import com.travelog.plan.exception.InvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface AttractionService {
    Page<Map<String, Object>> getAttractionList(AttractionRequestDto attractionRequestDto, Pageable pageable,String userId) throws Exception;
    Page<Map<String, Object>> getLikeAttractionList(Pageable pageable,String userId) throws Exception;
    AttractionDetailDto getAttractionDetail(int contentId,String userId) throws Exception;

    AttractionInfoDto findAttractionById(int contentId) throws InvalidException;

    void insertAttractionLike(Long contentId,String userId) throws Exception;
    void deleteAttractionLike(Long contentId,String userId) throws Exception;
}
