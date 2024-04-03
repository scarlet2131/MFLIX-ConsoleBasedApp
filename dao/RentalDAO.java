package dao;

import model.Rental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RentalDAO {
    boolean createRental(int userId, int movieId, LocalDate startDate, LocalDate dueDate, String status);
    List<Rental> getRentalHistoryForUser(int userId);
    boolean rentMovieAndUpdateBalance(int userId, int movieId, LocalDate startDate, LocalDate dueDate, BigDecimal cost);
}
