package utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalUtils {

    private RentalUtils() {
        // Private constructor to prevent instantiation
    }

    public static BigDecimal calculatePackageCost(int packageChoice) {
        switch (packageChoice) {
            case 1:
                return BigDecimal.valueOf(10); // 1 month
            case 2:
                return BigDecimal.valueOf(20); // 3 months
            case 3:
                return BigDecimal.valueOf(40); // 6 months
            case 4:
                return BigDecimal.valueOf(70); // 12 months
            default:
                return BigDecimal.ZERO; // Invalid choice
        }
    }

    public static LocalDate calculateDueDate(int packageChoice) {
        LocalDate startDate = LocalDate.now();
        switch (packageChoice) {
            case 1:
                return startDate.plusMonths(1);
            case 2:
                return startDate.plusMonths(3);
            case 3:
                return startDate.plusMonths(6);
            case 4:
                return startDate.plusMonths(12);
            default:
                return startDate; // Default to now (invalid choice handled elsewhere)
        }
    }

}

