package service;

import model.Review;

import java.util.List;

public interface ReviewService {
    public boolean addReview(Review review);
    public List<Review> getReviewsByMovieId(int movieId);
    public boolean updateReview(Review review);
    List<Review> getReviewsByUserId(int userId);
}
