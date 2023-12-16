package dashboard;

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

@WebServlet("/AcceptRejectBooking")
public class AcceptRejectBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AcceptRejectBookingServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingId = request.getParameter("bookingId");
        String action = request.getParameter("action");

        System.out.println("In the accept/reject booking section");
        try (Connection conn = DBConn.getConnection()) {
            boolean isUpdated = updateBookingStatus(conn, bookingId, action);

            if (isUpdated) {
                response.getWriter().write("Booking " + action + "ed successfully");
            } else {
                response.getWriter().write("Failed to " + action + " the booking");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    private boolean updateBookingStatus(Connection conn, String bookingId, String action) {
        try {
            // Prepare SQL statement to update the booking status
            String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, action);
                stmt.setString(2, bookingId);

                // Execute the update
                int rowsUpdated = stmt.executeUpdate();
                
                // Check if any rows were updated
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
