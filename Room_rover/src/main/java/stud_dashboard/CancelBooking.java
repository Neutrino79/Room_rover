package stud_dashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ConnInit.DBConn;

@WebServlet("/CancelBookings")
public class CancelBooking extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        // Update the status to 'cancelled' in the database
        boolean isCancelled = updateBookingStatus(bookingId, "cancelled");

        // Send a response indicating success or failure
        response.getWriter().write(isCancelled ? "1" : "0");
    }

    private boolean updateBookingStatus(int bookingId, String newStatus) {
        try (Connection conn = DBConn.getConnection()) {
            String sql = "UPDATE Booking SET status = ? WHERE booking_id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, newStatus);
                preparedStatement.setInt(2, bookingId);
                int rowsUpdated = preparedStatement.executeUpdate();

                // Return true if at least one row was updated
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
            return false;
        }
    }
}
