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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.ConnInit.DBConn;

@WebServlet("/BookHostelServlet")
public class BookHostel extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BookHostel() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        int studId = (int) session.getAttribute("StudID");
         System.out.println("In the Booking");
        int hostelId = Integer.parseInt(request.getParameter("hostelId"));
        // bookingTime = request.getParameter("bookingTime");
        String bookingDuration = request.getParameter("bookingDuration");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        BigDecimal price = new BigDecimal(request.getParameter("price"));

        

        // Insert data into the Booking table
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String query = "INSERT INTO Booking (stud_id, hostel_id, booking_time, start_date, end_date, price ,  book_duration) VALUES (?, ?, NOW(), ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, studId);
                preparedStatement.setInt(2, hostelId);
               // preparedStatement.setString(3, NOW());
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)", Locale.ENGLISH);
                java.util.Date endDateObj;
                try {
                    endDateObj = inputFormat.parse(endDate);
                } catch (ParseException e) {
                    throw new RuntimeException("Error parsing end date", e);
                }
                
                // Convert java.util.Date to java.sql.Date
                java.sql.Date sqlEndDate = new java.sql.Date(endDateObj.getTime());
                
                preparedStatement.setString(3, startDate);
                preparedStatement.setDate(4, sqlEndDate);
                preparedStatement.setBigDecimal(5, price);
                preparedStatement.setString(6, bookingDuration);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Booking successful
                    out.println("1");
                } else {
                    // Booking failed
                    out.println("0");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("0"); // Return 0 for any database-related errors
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
