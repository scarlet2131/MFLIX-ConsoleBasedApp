package model;

public class Rating {
    private int ratingID;
    private int movieID;
    private int userID;
    private int rating;

    // Constructor
    public Rating(int movieID, int userID, int rating) {
        this.movieID = movieID;
        this.userID = userID;
        this.rating = rating;
    }

    // Getters
    public int getRatingID() {
        return ratingID;
    }

    public int getMovieID() {
        return movieID;
    }

    public int getUserID() {
        return userID;
    }

    public int getRating() {
        return rating;
    }

    // Setters
    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
