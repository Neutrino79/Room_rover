package dashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ConnInit.DBConn;

//... (existing imports)

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;

@WebServlet("/AddHostel")
@MultipartConfig(
     fileSizeThreshold = 1024 * 1024, // 1 MB
     maxFileSize = 10 * 1024 * 1024,  // 10 MB
     maxRequestSize = 20 * 1024 * 1024  // 20 MB
)
public class Owner_Dashboard extends HttpServlet {
 private static final long serialVersionUID = 1L;

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     response.setContentType("text/html");

     PrintWriter out = response.getWriter();
     HttpSession session = request.getSession();
     int ID = (int) session.getAttribute("ID");

     if ("add".equals(request.getParameter("hostelEditMode")))
  	{
 	 System.out.print("herreeeeeee frommm neww hostel");
 	 // Retrieve data from the form
      String hostelName = request.getParameter("hostelName");
      String location = request.getParameter("location");
      int totalRooms = Integer.parseInt(request.getParameter("totalRooms"));
      String roomType = request.getParameter("roomType");
      int occupied = Integer.parseInt(request.getParameter("currentlyFilled"));
      double price = Double.parseDouble(request.getParameter("price"));
      String[] selectedAmenities = request.getParameterValues("amenities[]");
      String amenitiesString = String.join(", ", selectedAmenities);

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
      Connection conn = null;
         try {conn = DBConn.getConnection();
         String insertQuery = "INSERT INTO hostels (hostel_name, location, owner_id, total_rooms, room_type, occupied, Is_full, price, amenities, status, documents) " +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
         PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
         preparedStatement.setString(1, hostelName);
         preparedStatement.setString(2, location);
         preparedStatement.setInt(3, ID); // Use the owner's ID from your session
         preparedStatement.setInt(4, totalRooms);
         preparedStatement.setString(5, roomType);
         preparedStatement.setInt(6, occupied);
         preparedStatement.setBoolean(7, isFull);
         preparedStatement.setDouble(8, price);
         preparedStatement.setString(9, amenitiesString);
         preparedStatement.setString(10, "pending"); // Default status is "pending"

         // Save the document file locally
         Part documentPart = request.getPart("documents");
         String fileName = getFileName(documentPart);
         String uploadPath = "/Users/atharvhiremath/eclipse-workspace/git/Room_rover/src/files"; // Change this to your desired upload directory

         // Check if the directory exists, create it if not
         Files.createDirectories(Path.of(uploadPath));

         Path filePath = Paths.get(uploadPath, fileName);

         try (InputStream fileContent = documentPart.getInputStream()) {
             Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
         }

         // Store the file path in the database
         preparedStatement.setString(11, filePath.toString());

         int rowsAffected = preparedStatement.executeUpdate();

         if (rowsAffected > 0) {
        	 out.println("<script>alert('Hostel data saved successfully!'); window.location.href='owner_dashboard.html';</script>");
         } else {
        	 out.println("<script>alert('Error: Hostel data could not be saved.'); window.history.back();</script>");
         }
             conn.close();
         } catch (SQLException e) {
             e.printStackTrace();
             out.println("Error: Database error occurred while saving hostel data.");
             out.println("SQL State: " + e.getSQLState());
             out.println("Error Message: " + e.getMessage());
         } catch (Exception e) {
             e.printStackTrace();
             out.println("Error: Exception occurred while saving hostel data. Please check your code and logs for more details." + e.getMessage());
         } finally {
             try {
                 conn.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
  		}

     else if ("update".equals(request.getParameter("hostelEditMode"))) {

    	 System.out.print("herreeeeeee frommm UPDATE hostel");
     }
 }

 private String getFileName(Part part) {
     String contentDisposition = part.getHeader("content-disposition");
     String[] tokens = contentDisposition.split(";");
     for (String token : tokens) {
         if (token.trim().startsWith("filename")) {
             return token.substring(token.indexOf("=") + 2, token.length() - 1);
         }
     }
     return "";
 }
}

