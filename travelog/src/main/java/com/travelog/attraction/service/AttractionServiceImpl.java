package com.travelog.attraction.service;

import com.travelog.attraction.dao.AttractionDao;
import com.travelog.attraction.dto.AttractionDetailDto;
import com.travelog.attraction.dto.AttractionInfoDto;
import com.travelog.attraction.dto.AttractionRequestDto;
import com.travelog.plan.exception.InvalidException;
import com.travelog.util.RequestList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class AttractionServiceImpl implements  AttractionService{

    private final AttractionDao attractionDao;

    @Autowired
    public AttractionServiceImpl(AttractionDao attractionDao) {
        this.attractionDao = attractionDao;
    }

    @Override
    public Page<Map<String, Object>> getAttractionList(AttractionRequestDto attractionRequestDto, Pageable pageable,String userId) throws Exception {

        //빌더 패턴으로 data,pageable 파라미터에 데이터 주입
        RequestList<?> requestList = RequestList.builder()
                .data(attractionRequestDto)
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = attractionDao.getAttractionList(requestList,userId);

        return new PageImpl<>(content);
    }

    @Override
    public Page<Map<String, Object>> getLikeAttractionList(Pageable pageable, String userId) throws Exception {
        //빌더 패턴으로 data,pageable 파라미터에 데이터 주입
        RequestList<?> requestList = RequestList.builder()
                .pageable(pageable)
                .build();

        List<Map<String,Object>> content = attractionDao.getLikeAttractionList(requestList,userId);

        return new PageImpl<>(content);
    }

    @Override
    public AttractionDetailDto getAttractionDetail(int contentId,String userId) throws SQLException {
        return attractionDao.getAttractionDetail(contentId,userId);
    }

    @Override
    @Transactional
    public AttractionInfoDto findAttractionById(int contentId) throws InvalidException {

        AttractionInfoDto attraction = attractionDao.findAttractionById(contentId);
        return attraction;
    }

    @Override
    public void insertAttractionLike(Long contentId, String userId) throws Exception {
        // #1 : 관광지 좋아요 삽입
        attractionDao.insertAttractionLike(contentId,userId);

        // #2 : 해당 관광지에 likes+1
        attractionDao.incrementAttrLikes(contentId);
    }

    @Override
    public void deleteAttractionLike(Long contentId, String userId) throws Exception {
        // #1 : 관광지 좋아요 삭제
        attractionDao.deleteAttractionLike(contentId,userId);

        // #2 : 해당 관광지에 likes-1
        attractionDao.decrementAttrLikes(contentId);
    }
}
