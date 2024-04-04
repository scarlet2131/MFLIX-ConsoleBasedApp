package service;

import dao.RentalDAO;
import dao.RentalDAOImpl;
import dto.RentalDTO;
import model.Movie;
import model.Rental;
import utility.RentalUtils;
import utility.SessionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RentalServiceImpl implements RentalService{

    private RentalDAO rentalDAO = new RentalDAOImpl();

    @Override
    public List<Rental> getRentalHistory(int userId) {
        return rentalDAO.getRentalHistoryForUser(userId);
    }

    @Override
    public String rentMovie(RentalDTO rentalDTO) {

        // Attempt to create rental and update balance
        boolean success = rentalDAO.rentMovieAndUpdateBalance(rentalDTO.getUserId(), rentalDTO.getMovieId(), rentalDTO.getStartDate(), rentalDTO.getDueDate(), rentalDTO.getCost());

        if (success) {
            SessionManager.updateBalance(rentalDTO.getCost().negate());
            return "Movie rented successfully. Enjoy your movie!";
        } else {
            return "Failed to rent movie or update balance. Please try again.";
        }
    }



}
