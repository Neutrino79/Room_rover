package dashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ConnInit.DBConn;

@WebServlet("/FetchRequestInfo")
public class FetchRequestInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FetchRequestInfoServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

       
        Connection conn = null;
        

        try {
        	conn = DBConn.getConnection();
            // Get the booking ID from the request
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));

            // Execute SQL query to fetch detailed data for the specified booking ID
            String sql = "SELECT b.*, s.*, h.* " +
                         "FROM Booking b " +
                         "JOIN Students s ON b.stud_id = s.student_id " +
                         "JOIN Hostels h ON b.hostel_id = h.hostel_id " +
                         "WHERE b.booking_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, bookingId);
            ResultSet resultSet = statement.executeQuery();

            // Convert ResultSet to JSON
            if (resultSet.next()) {
                // Construct a JSON object with the required fields
                // You can customize this based on your needs
                out.print("{ \"booking_id\": " + resultSet.getInt("booking_id") +
                          ", \"status\": \"" + resultSet.getString("status") +
                          "\", \"booking_time\": \"" + resultSet.getString("booking_time") +
                          "\", \"payment_details\": \"" + resultSet.getString("payment_details") +
                          "\", \"start_date\": \"" + resultSet.getString("start_date") +
                          "\", \"end_date\": \"" + resultSet.getString("end_date") +
                          "\", \"book_duration\": \"" + resultSet.getString("book_duration") +
                          "\", \"student\": {\"student_id\": " + resultSet.getInt("student_id") +
                          ", \"student_name\": \"" + resultSet.getString("student_name") +
                          "\", \"email\": \"" + resultSet.getString("email") +
                          "\", \"phone\": \"" + resultSet.getString("phone") +
                          "\", \"gender\": \"" + resultSet.getString("gender") +
                          "\"}, \"hostel\": {\"hostel_id\": " + resultSet.getInt("hostel_id") +
                          ", \"hostel_name\": \"" + resultSet.getString("hostel_name") +
                          "\", \"location\": \"" + resultSet.getString("location") +
                          "\", \"total_rooms\": " + resultSet.getInt("total_rooms") +
                          ", \"room_type\": \"" + resultSet.getString("room_type") +
                          "\", \"price\": " + resultSet.getDouble("price") +
                          ", \"status\": \"" + resultSet.getString("status") +
                          "\", \"address\": \"" + resultSet.getString("address") +
                          "\", \"landmark\": \"" + resultSet.getString("landmark") +
                          "\", \"gender\": \"" + resultSet.getString("gender") +
                          "\"}}");
            }

            // Close resources
            resultSet.close();
            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
