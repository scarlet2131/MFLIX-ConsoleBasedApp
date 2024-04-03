package service;

import model.Movie;
import model.Rental;

import java.math.BigDecimal;
import java.util.List;

public interface RentalService {
    List<Rental> getRentalHistory(int userId);
    String rentMovie(int userId, Movie movie, int packageChoice, BigDecimal cost);
}
