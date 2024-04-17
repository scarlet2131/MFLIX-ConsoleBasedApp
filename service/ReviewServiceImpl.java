package service;

import dao.MovieDAO;
import dao.MovieDAOImpl;
import dao.ReviewDAO;
import dao.ReviewDAOImpl;
import model.Review;
import utility.ConsoleUtil;

import java.util.List;

public class ReviewServiceImpl implements ReviewService{

    private ReviewDAO reviewDAO = new ReviewDAOImpl();

    private MovieServiceImpl movieService = new MovieServiceImpl();

    @Override
    public boolean addReview(Review review) {
        if(!movieService.checkMovieAvailabilty(review.getMovieID())) return false;
        return reviewDAO.addReview(review);
    }
    @Override
    public List<Review> getReviewsByMovieId(int movieId) {
        return reviewDAO.getReviewsByMovieId(movieId);
    }
    @Override
    public boolean updateReview(Review review) {
        return reviewDAO.updateReview(review);
    }
    @Override
    public List<Review> getReviewsByUserId(int userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }
}
