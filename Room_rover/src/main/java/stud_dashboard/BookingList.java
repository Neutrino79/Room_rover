package stud_dashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ConnInit.DBConn;

@WebServlet("/FetchBooking")
public class BookingList extends HttpServlet {
    // ... (existing code)

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	String action = request.getParameter("action");
    	int studentID=0;
    	if ("student".equals(action)) {
    		studentID= (int) session.getAttribute("StudID");
        } 
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        conn = DBConn.getConnection();

        try {
            conn = DBConn.getConnection();
            String sql;

            if ("owner".equals(action)) {
                // If student ID is null, fetch bookings for all students
                sql = "SELECT b.*, s.student_name, s.gender, s.phone FROM Booking b " +
                        "JOIN Students s ON b.stud_id = s.student_id";
            } else {
                // If student ID is present, fetch bookings for that student
                sql = "SELECT b.*, s.student_name, s.gender, s.phone FROM Booking b " +
                        "JOIN Students s ON b.stud_id = s.student_id " +
                        "WHERE b.stud_id = ?";
            }

            PreparedStatement statement = conn.prepareStatement(sql);

            if("student".equals(action)) {
                // If student ID is present, set the parameter
                statement.setInt(1, studentID);
            }

            ResultSet resultSet = statement.executeQuery();

            // Convert result set to JSON
            out.print(resultSetToJSON(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            out.close();
        }
    }

    private String resultSetToJSON(ResultSet resultSet) throws SQLException {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        while (resultSet.next()) {
            if (!first) {
                json.append(",");
            }
            int bookingId = resultSet.getInt("booking_id");
            int studId = resultSet.getInt("stud_id");
            int hostelId = resultSet.getInt("hostel_id");
            BigDecimal price = resultSet.getBigDecimal("price");
            String status = resultSet.getString("status");
            String bookingTime = resultSet.getString("booking_time");
            String paymentDetails = resultSet.getString("payment_details");
            String paymentTime = resultSet.getString("payment_time");
            String startDate = resultSet.getString("start_date");
            String endDate = resultSet.getString("end_date");
            String bookDuration = resultSet.getString("book_duration");

            // Student-specific data
            String studentName = resultSet.getString("student_name");
            String gender = resultSet.getString("gender");
            String phone = resultSet.getString("phone");

            // Fetch hostel details
            Map<String, String> hostelDetails = getHostelDetails(hostelId);

            json.append("{");
            json.append("\"booking_id\":").append(bookingId).append(",");
            json.append("\"stud_id\":").append(studId).append(",");
            json.append("\"hostel_id\":").append(hostelId).append(",");
            json.append("\"hostel_name\":\"").append(hostelDetails.get("hostel_name")).append("\",");
            json.append("\"location\":\"").append(hostelDetails.get("location")).append("\",");
            json.append("\"room_type\":\"").append(hostelDetails.get("room_type")).append("\",");
            json.append("\"address\":\"").append(hostelDetails.get("address")).append("\",");
            json.append("\"price\":").append(price).append(",");
            json.append("\"status\":\"").append(status).append("\",");
            json.append("\"booking_time\":\"").append(bookingTime).append("\",");
            json.append("\"payment_details\":\"").append(paymentDetails).append("\",");
            json.append("\"payment_time\":\"").append(paymentTime).append("\",");
            json.append("\"start_date\":\"").append(startDate).append("\",");
            json.append("\"end_date\":\"").append(endDate).append("\",");
            json.append("\"book_duration\":\"").append(bookDuration).append("\",");
            json.append("\"student_name\":\"").append(studentName).append("\",");
            json.append("\"gender\":\"").append(gender).append("\",");
            json.append("\"phone\":\"").append(phone).append("\"");
            json.append("}");

            first = false;
        }
        json.append("]");
        System.out.println("JSON Response: " + json.toString());
        return json.toString();
       
    }

    // ... (existing code)



    private Map<String, String> getHostelDetails(int hostelId) {
        Map<String, String> hostelDetails = new HashMap<>();
        try (Connection conn = DBConn.getConnection()) {
            String sql = "SELECT hostel_name, location, room_type, address FROM hostels WHERE hostel_id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, hostelId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hostelDetails.put("hostel_name", resultSet.getString("hostel_name"));
                        System.out.println(resultSet.getString("hostel_name"));
                        hostelDetails.put("location", resultSet.getString("location"));
                        hostelDetails.put("room_type", resultSet.getString("room_type"));
                        hostelDetails.put("address", resultSet.getString("address"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hostelDetails;
    }


}
