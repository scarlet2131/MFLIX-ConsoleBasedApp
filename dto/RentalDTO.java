package dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalDTO {
    private int userId;
    private int movieId;
    private LocalDate startDate;
    private LocalDate dueDate;
    private BigDecimal cost;

    // Constructor, Getters, and Setters
    public RentalDTO(int userId, int movieId, LocalDate startDate, LocalDate dueDate, BigDecimal cost) {
        this.userId = userId;
        this.movieId = movieId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.cost = cost;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    // Getters and Setters

}

