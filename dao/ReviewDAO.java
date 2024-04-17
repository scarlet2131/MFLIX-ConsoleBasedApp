package dao;

import model.Review;

import java.util.List;

public interface ReviewDAO {
    boolean addReview(Review review);
    List<Review> getReviewsByMovieId(int movieId);
    boolean updateReview(Review review);
    List<Review> getReviewsByUserId(int userId);

}
