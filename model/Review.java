package model;

import java.util.Date;

public class Review {
    private int reviewID;
    private int movieID;
    private int userID;
    private String review;
    private Date reviewDate;

    // Constructor
    public Review(int reviewID, int movieID, int userID, String review, Date reviewDate) {
        this.reviewID = reviewID;
        this.movieID = movieID;
        this.userID = userID;
        this.review = review;
        this.reviewDate = reviewDate;
    }

    // Getters and setters
    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }
    public int getMovieID() { return movieID; }
    public void setMovieID(int movieID) { this.movieID = movieID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }
}
