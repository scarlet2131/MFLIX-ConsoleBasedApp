package model;

import java.time.LocalDate;

public class Rental {
    private int rentalId; // corresponds to RentalID in the database
    private int userId; // corresponds to UserID
    private int movieId; // corresponds to MovieID
    private LocalDate startDate; // corresponds to StartDate
    private LocalDate dueDate; // corresponds to DueDate
    private String status; // corresponds to Status, using String for simplicity

    // Constructor
    public Rental(int userId, int movieId, LocalDate startDate, LocalDate dueDate, String status) {
        this.userId = userId;
        this.movieId = movieId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    public Rental(int rentalId, int userId, int movieId, LocalDate startDate, LocalDate dueDate, String status) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.movieId = movieId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
