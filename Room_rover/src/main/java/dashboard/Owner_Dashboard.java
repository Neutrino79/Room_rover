package dashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ConnInit.DBConn;

@WebServlet("/AddHostel")
public class Owner_Dashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
       // String email = (String) session.getAttribute("email"); 
        int ID =(int) session.getAttribute("ID");

        
        
        // Retrieve data from the form
        String hostelName = request.getParameter("hostelName");
        String location = request.getParameter("location");
        int totalRooms = Integer.parseInt(request.getParameter("totalRooms"));
        String roomType = request.getParameter("roomType");
        int occupied = Integer.parseInt(request.getParameter("currentlyFilled"));
        double price = Double.parseDouble(request.getParameter("price"));
        String amenities = request.getParameter("amenities");

        // Calculate the total students that can live in the hostel
        int totalStudentsCanLive = 0;
        if (roomType.equals("single")) {
            totalStudentsCanLive = 1 * totalRooms;
        } else if (roomType.equals("2-sharing")) {
            totalStudentsCanLive = 2 * totalRooms;
        } else if (roomType.equals("3-sharing")) {
            totalStudentsCanLive = 3 * totalRooms;
        } else if (roomType.equals("4-sharing")) {
            totalStudentsCanLive = 4 * totalRooms;
        }

        // Check if the hostel is full
        boolean isFull = (occupied >= totalStudentsCanLive);

        // Validate occupied students
        if (occupied > totalStudentsCanLive) {
            out.println("Error: Occupied students cannot exceed total students who can live.");
            return; // Stop processing
        }

        // Insert data into the database
        
        Connection conn=null;
        try 
        {
            
        	conn= DBConn.getConnection();
            String insertQuery = "INSERT INTO hostels (hostel_name, location, owner_id, total_rooms, room_type, occupied, Is_full, price, amenities, status, documents) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, hostelName);
            preparedStatement.setString(2, location);
            preparedStatement.setInt(3, ID); // Use the owner's ID from your session
            preparedStatement.setInt(4, totalRooms);
            preparedStatement.setString(5, roomType);
            preparedStatement.setInt(6, occupied);
            preparedStatement.setBoolean(7, isFull);
            preparedStatement.setDouble(8, price);
            preparedStatement.setString(9, amenities);
            preparedStatement.setString(10, "pending"); // Default status is "pending"
            
            // Insert the document file as a BLOB
            InputStream documentStream = request.getPart("documents").getInputStream();
            preparedStatement.setBinaryStream(11, documentStream);
            

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                out.println("Hostel data saved successfully!");
            } else {
                out.println("Error: Hostel data could not be saved.");
            }
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Error: Database error occurred while saving hostel data.");
            out.println("SQL State: " + e.getSQLState());
            out.println("Error Message: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: Exception occurred while saving hostel data. Please check your code and logs for more details.");
        } finally {
        	try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
