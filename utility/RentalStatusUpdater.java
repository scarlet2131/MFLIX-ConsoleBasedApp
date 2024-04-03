package utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RentalStatusUpdater {
    public void startScheduledStatusUpdate() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule a task to run after an initial delay and then at fixed intervals
        // For example, run the task immediately upon starting, then every 24 hours
        executorService.scheduleAtFixedRate(this::updateRentalStatuses, 0, 24, TimeUnit.HOURS);
    }

    private void updateRentalStatuses() {
        String sql = "UPDATE rentals SET Status = 'UNAVAILABLE' WHERE DueDate < ? AND Status = 'RENTED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, LocalDate.now());
            int affectedRows = pstmt.executeUpdate();

            System.out.println("Updated " + affectedRows + " rental statuses to UNAVAILABLE.");
        } catch ( SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
