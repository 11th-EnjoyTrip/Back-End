package com.travelog.review.service;

import com.travelog.review.dao.ReviewDao;
import com.travelog.review.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{
    private final ReviewDao reviewDao;
    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
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
    public void update(String text, String content_id) {
        reviewDao.update(text, content_id);
    }

    @Override
    public String getIdByContent_id(String content_id) {
        return reviewDao.getIdByContent_id(content_id);
    }

    @Override
    public List<ReviewDto> getReviewsByContentId(String content_id) throws Exception {
        return reviewDao.getReviewsByContentId(content_id);
    }

    @Override
    public String getIdByReview_id(int review_id) {
        return reviewDao.getIdByReview_id(review_id);
    }

    @Override
    public void delete(int review_id) {
        reviewDao.delete(review_id);
    }
}
