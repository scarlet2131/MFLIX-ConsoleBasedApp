package dao;

import model.Rating;

public interface RatingDAO {
    boolean addRating(Rating rating);
    double getAverageRating(int movieID);
}
