package dashboard;

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

    // Define HostelData as a static inner class
    public static class HostelData {
        private String hostelName;
        private String location;
        private int totalRooms;
        private String roomType;
        private int occupied;
        private double price;
        private String amenitiesString;
        private String documentPath;

        // Constructors
        public HostelData() {
        }

        public HostelData(String hostelName, String location, int totalRooms, String roomType, int occupied,
                          double price, String amenitiesString, String documentPath) {
            this.hostelName = hostelName;
            this.location = location;
            this.totalRooms = totalRooms;
            this.roomType = roomType;
            this.occupied = occupied;
            this.price = price;
            this.amenitiesString = amenitiesString;
            this.documentPath = documentPath;
        }

        // Getters and setters
        public String getHostelName() {
            return hostelName;
        }

        public void setHostelName(String hostelName) {
            this.hostelName = hostelName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getTotalRooms() {
            return totalRooms;
        }

        public void setTotalRooms(int totalRooms) {
            this.totalRooms = totalRooms;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public int getOccupied() {
            return occupied;
        }

        public void setOccupied(int occupied) {
            this.occupied = occupied;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getAmenitiesString() {
            return amenitiesString;
        }

        public void setAmenitiesString(String amenitiesString) {
            this.amenitiesString = amenitiesString;
        }

        public String getDocumentPath() {
            return documentPath;
        }

        public void setDocumentPath(String documentPath) {
            this.documentPath = documentPath;
        }
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(request, response);
		System.out.println("i am in the fill servlt");
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        int hostelId = Integer.parseInt(request.getParameter("hostelIdToPrefill"));

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
                
             //   ObjectMapper objectMapper = new ObjectMapper();
             //   String jsonResponse = objectMapper.writeValueAsString(hostelData);

          //      out.print(jsonResponse);
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
        }
    }
	

}
