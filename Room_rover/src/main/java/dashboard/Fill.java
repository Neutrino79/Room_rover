package dashboard;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ConnInit.DBConn;


@WebServlet("/Prefill")
public class Fill extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Fill() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("i am in the doget servlt");
    }
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	//doGet(request, response);
		//response.setContentType("application/json");
		
		System.out.println("i am in the fill servlt");
		PrintWriter out = response.getWriter();
        String hostelIdParam = request.getParameter("hostelId");

        if (hostelIdParam != null && !hostelIdParam.isEmpty()) {
            try {
                int hostelId = Integer.parseInt(hostelIdParam);
                System.out.println("Hostel id = " + hostelId);
            } catch (NumberFormatException e) {
                // Handle the case where "hostelId" is not a valid integer
                System.err.println("Invalid hostelId format: " + hostelIdParam);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else {
            // Handle the case where "hostelId" is null or empty.
            // You might want to set a default value or return an error response.
            System.err.println("Hostel id is missing or empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        /*
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String selectQuery = "SELECT * FROM hostels WHERE hostel_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
            preparedStatement.setInt(1, hostelId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	
                String hostelName = resultSet.getString("hostel_name");
                System.out.println("hostel name ="+hostelName);
                String location = resultSet.getString("location");
                int totalRooms = resultSet.getInt("total_rooms");
                String roomType = resultSet.getString("room_type");
                int occupied = resultSet.getInt("occupied");
                double price = resultSet.getDouble("price");
                String amenitiesString = resultSet.getString("amenities");
                String documentPath = resultSet.getString("documents");

                HostelData hostelData = new HostelData(hostelName, location, totalRooms, roomType, occupied, price, amenitiesString, documentPath);
                
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(hostelData);

                out.print(jsonResponse);
            } else {
                out.print("{\"error\": \"No data found for hostelId: " + hostelId + "\"}");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            out.print("{\"error\": \"Database error occurred while fetching data.\"}");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
	

}