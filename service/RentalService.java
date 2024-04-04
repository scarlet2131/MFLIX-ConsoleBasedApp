package service;

import dto.RentalDTO;
import model.Movie;
import model.Rental;

import java.math.BigDecimal;
import java.util.List;

public interface RentalService {
    List<Rental> getRentalHistory(int userId);
    String rentMovie(RentalDTO rentalDTO);
}
