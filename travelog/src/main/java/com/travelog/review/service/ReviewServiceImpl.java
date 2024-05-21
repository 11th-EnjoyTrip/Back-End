package com.travelog.review.service;

import com.travelog.member.service.MemberServiceImpl;
import com.travelog.review.dao.ReviewDao;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import com.travelog.review.dto.ReviewLikeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{
    private final ReviewDao reviewDao;
    private final MemberServiceImpl memberService;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, MemberServiceImpl memberService) {
        this.reviewDao = reviewDao;
        this.memberService = memberService;
    }

    @Override
    public void write(ReviewDto reviewDto) throws Exception {
        reviewDao.write(reviewDto);
    }

    @Override
    public List<ReviewDto> getReviewsByUserid(String user_id) throws Exception {
        return reviewDao.getReviewsByUserid(user_id);
    }

    @Override
    public void update(String text, int review_id) {
        reviewDao.update(text, review_id);
    }

    @Override
    public String getIdByContent_id(String content_id) {
        return reviewDao.getIdByContent_id(content_id);
    }

    @Override
    public List<ReviewDto> getReviewsByContentId(String content_id) throws Exception {
        return reviewDao.getReviewsByContentId(content_id);
    }
    public List<ResponseReviewDto> getResponseReviewsByUserid(String user_id) throws Exception {
        return reviewDao.getResponseReviewsByUserid(user_id);
    }

    @Override
    public List<ReviewDto> getTopReview() throws SQLException {
        return reviewDao.getTopReview();
    }

    @Override
    public List<ResponseReviewDto[]> getReviewLikeByUserid(String userid) throws Exception {
        List<ResponseReviewDto[]> result = new ArrayList<>();
        List<ResponseReviewDto> reviews = getResponseReviewsByUserid(userid);
        System.out.println("결과로 받아온 리뷰 개수 : " + reviews.size());
        int listIdx = 0;
        int idx = 0;

        result.add(new ResponseReviewDto[10]);
        for (ResponseReviewDto review : reviews) {
            result.get(listIdx)[idx] = review;
            if(++idx == 10) {
                idx = 0;
                result.add(new ResponseReviewDto[10]);
                listIdx++;
            }
        }
        System.out.println(result);

        return result;
    }

    @Override
    public String getIdByReview_id(int review_id) {
        return reviewDao.getIdByReview_id(review_id);
    }

    @Override
    public void delete(int review_id) {
        reviewDao.delete(review_id);
    }

    @Override
    public void addLike(int review_id, String userid) {
        ReviewLikeDto reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setReview_id(review_id);
        reviewLikeDto.setUserid(userid);
        reviewDao.addLike(reviewLikeDto);
        reviewDao.updateLike(review_id);
    }
    public void updateLike(int review_id) {
        reviewDao.updateLike(review_id);
    }
    @Override
    public void deleteLike(int review_id, String userid) {
        ReviewLikeDto reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setReview_id(review_id);
        reviewLikeDto.setUserid(userid);
        reviewDao.deleteLike(reviewLikeDto);
    }


}
