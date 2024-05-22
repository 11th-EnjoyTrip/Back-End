package com.travelog.review.service;

import com.travelog.member.service.MemberServiceImpl;
import com.travelog.review.dao.ReviewDao;
import com.travelog.review.dto.MyPageReviewDto;
import com.travelog.review.dto.ResponseReviewDto;
import com.travelog.review.dto.ReviewDto;
import com.travelog.review.dto.ReviewLikeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
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
    public List<MyPageReviewDto[]> getReviewsByUserid(String user_id) throws Exception {
        // pagination;
//        return reviewDao.getReviewsByUserid(user_id);
        List<MyPageReviewDto[]> result = new ArrayList<>();
        List<MyPageReviewDto> reviews = reviewDao.getMyPageReviewsByContentId(user_id);

        int listIdx = 0;
        int idx = 0;

        result.add(new MyPageReviewDto[10]);

        for (MyPageReviewDto review : reviews) {
            if (idx == 10) {
                idx = 0;
                result.add(new MyPageReviewDto[10]);
                listIdx++;
            }
            result.get(listIdx)[idx] = review;
            idx++;

        }
        return result;
    }

    @Override
    public void update(String text, int review_id) {
        reviewDao.update(text, review_id);
    }

    @Override
    public List<ResponseReviewDto[]> getResponseReviewsByContentId(String content_id) throws Exception {
        List<ResponseReviewDto[]> result = new ArrayList<>();
        List<ResponseReviewDto> reviews = reviewDao.getResponseReviewsByContentId(content_id);

        int listIdx = 0;
        int idx = 0;

        result.add(new ResponseReviewDto[10]);

        for (ResponseReviewDto review : reviews) {
            if (idx == 10) {
                idx = 0;
                result.add(new ResponseReviewDto[10]);
                listIdx++;
            }
            result.get(listIdx)[idx] = review;
            idx++;

        }
        return result;
    }

    @Override
    public List<MyPageReviewDto[]> getReviewsByContentId(String content_id) throws Exception {
        List<MyPageReviewDto[]> result = new ArrayList<>();
        List<MyPageReviewDto> reviews = reviewDao.getReviewsByContentId(content_id);

        int listIdx = 0;
        int idx = 0;

        result.add(new MyPageReviewDto[10]);

        for (MyPageReviewDto review : reviews) {
            if (idx == 10) {
                idx = 0;
                result.add(new MyPageReviewDto[10]);
                listIdx++;
            }
            result.get(listIdx)[idx] = review;
            idx++;

        }
        return result;
    }

    @Override
    public List<ReviewDto> getTopReview() throws SQLException {
        return reviewDao.getTopReview();
    }

    @Override
    public List<MyPageReviewDto[]> getReviewLikeByUserid(String userid) throws Exception {
        List<MyPageReviewDto[]> result = new ArrayList<>();
        List<MyPageReviewDto> reviews = reviewDao.getMyPageReviewsByContentId(userid);
        int listIdx = 0;
        int idx = 0;

        result.add(new MyPageReviewDto[10]);

        for (MyPageReviewDto review : reviews) {
            if (idx == 10) {
                idx = 0;
                result.add(new MyPageReviewDto[10]);
                listIdx++;
            }
            result.get(listIdx)[idx] = review;
            idx++;

        }
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
        reviewDao.addLike(new ReviewLikeDto(review_id, userid));
        reviewDao.updateLike(review_id);
    }

    @Override
    public void updateLike(int review_id) {
        reviewDao.updateLike(review_id);
    }

    @Override
    public void deleteLike(int review_id, String userid) {
        reviewDao.deleteLike(new ReviewLikeDto(review_id , userid));
    }


}
