package dashboard;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.ConnInit.DBConn;

@WebServlet("/HostelList")
public class HostelList extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
    	HttpSession session = request.getSession();
        int ID = (int) session.getAttribute("ID");
    	response.setContentType("application/json");
    	PrintWriter out = response.getWriter();
    	 Connection conn = null;
    	 conn=DBConn.getConnection();
    	 
    	 
    	 String action = request.getParameter("action");
    	 
         if ("delete".equals(action)) {
             int hostelId = Integer.parseInt(request.getParameter("hostelId"));
             deleteHostel(conn,hostelId, response);
         }
         try 
         {            
             String sql = "select * from hostels where owner_id ="+ID;
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();
            
             // Convert result set to JSON
             out.print(resultSetToJSON(resultSet));
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         finally 
         {
             // Close the database connection
             if (conn != null) 
             {
                 try 
                 {
                     conn.close();
                 }
                 catch (SQLException e)
                 {
                     e.printStackTrace();
                 }
             }
             out.close();
         }
         
         
         
         
       //------------ delete code ------------//

         
       
      
                
    }

    private String resultSetToJSON(ResultSet resultSet) throws SQLException {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        while (resultSet.next()) {
            if (!first) {
                json.append(",");
            }
            int hostelId = resultSet.getInt("hostel_id");
            String hostelName = resultSet.getString("hostel_name");
            String location = resultSet.getString("location");
            String status = resultSet.getString("status");
            int total_rooms = resultSet.getInt("total_rooms");
            String type = resultSet.getString("room_type");
            int room_type;
            int occupied = resultSet.getInt("occupied");
            switch (type) {
                case "Single":
                    room_type = 1;
                    break;
                case "2-sharing":
                    room_type = 2;
                    break;
                case "3-sharing":
                    room_type = 3;
                    break;
                case "4-sharing":
                    room_type = 4;
                    break;
                default:
                    room_type = 0;
            }

            int remainingSpace = (total_rooms * room_type) - occupied;
            json.append("{");
            json.append("\"hostel_id\":").append(hostelId).append(",");
            json.append("\"hostel_name\":\"").append(hostelName).append("\",");
            json.append("\"location\":\"").append(location).append("\",");
            json.append("\"status\":\"").append(status).append("\",");
            json.append("\"remaining_space\":").append(remainingSpace);
            json.append("}");

            first = false;
        }
        json.append("]");
        return json.toString();
        
    }   
    
    
    private void deleteHostel(Connection conn, int hostelId, HttpServletResponse response) throws IOException {
    	response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String deleteSql = "DELETE FROM hostels WHERE hostel_id = ?";
            try (PreparedStatement deleteStatement = conn.prepareStatement(deleteSql)) {
            	deleteStatement.setInt(1, hostelId);
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Indicate success in the response
                    out.print("{\"message\": \"Hostel deleted successfully!\"}");
                } else {
                    // Indicate failure in the response
                    out.print("{\"message\": \"Hostel not found or could not be deleted.\"}");
                }

                // Commit the transaction
                conn.commit();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception and send an error response
           // out.print("{\"message\": \"Error deleting hostel: " + e.getMessage() + "\"}");
        } finally {
            // Close the PrintWriter
            if (out != null) {
                out.close();
            }

            // Close the database connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}