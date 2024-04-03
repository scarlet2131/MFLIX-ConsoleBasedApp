package service;

import dao.RentalDAO;
import dao.RentalDAOImpl;
import model.Movie;
import model.Rental;

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
    public String rentMovie(int userId, Movie movie, int packageChoice, BigDecimal cost) {
        // Calculate rental period based on package choice
        LocalDate startDate = LocalDate.now();
        LocalDate dueDate = calculateDueDate(packageChoice); // Implement based on packageChoice

        // Attempt to create rental and update balance
        boolean success = rentalDAO.rentMovieAndUpdateBalance(userId, movie.getMovieId(), startDate, dueDate, cost);

        if (success) {
            return "Movie rented successfully. Enjoy your movie!";
        } else {
            return "Failed to rent movie or update balance. Please try again.";
        }
    }

    public LocalDate calculateDueDate(int packageChoice) {
        LocalDate startDate = LocalDate.now();
        LocalDate dueDate;

        switch (packageChoice) {
            case 1: // 1 Month
                dueDate = startDate.plusMonths(1);
                break;
            case 2: // 3 Months
                dueDate = startDate.plusMonths(3);
                break;
            case 3: // 6 Months
                dueDate = startDate.plusMonths(6);
                break;
            case 4: // 12 Months
                dueDate = startDate.plusMonths(12);
                break;
            default:
                dueDate = startDate; // Default case, should not happen
        }

        return dueDate;
    }

}
