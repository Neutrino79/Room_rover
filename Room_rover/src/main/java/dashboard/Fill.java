package dashboard;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.RequestDispatcher;
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
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("I am in the doGet servlet");
    }

    
    public static class Hostel {
        private String hostelName;
        private String location;
        private int totalRooms;
        private String roomType;
        private int occupied;
        private double price;
        private String amenitiesString;
        private String documentPath;
        private String address;
        private String landmark;
        private String gender;
      //  private int contains;

        public Hostel(String hostelName, String location, int totalRooms, String roomType, int occupied,
                      double price, String amenitiesString, String documentPath, String address,String landmark,String gender) {
            this.hostelName = hostelName;
            this.location = location;
            this.totalRooms = totalRooms;
            this.roomType = roomType;
            this.occupied = occupied;
            this.price = price;
            this.amenitiesString = amenitiesString;
            this.documentPath = documentPath;
            this.address=address;
            this.landmark=landmark;
            this.gender=gender;
        }

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
        public String getaddress() {
           return address;
        }
        public void setaddress(String address) {
            this.address = address;
        }
        public String getlandmark() {
            return landmark;
         }
         public void setlandmark(String landmark) {
             this.landmark = landmark;
         }
         public String getgender() {
             return gender;
          }
          public void setgender(String gender) {
              this.gender = gender;
          }
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("I am in the fill servlet");

        String hostelIdToPrefill = request.getParameter("hostelIdToPrefill");


        if (hostelIdToPrefill != null && !hostelIdToPrefill.isEmpty()) {
            try {
                int hostelId = Integer.parseInt(hostelIdToPrefill);
                System.out.println("Hostel id = " + hostelId);

                Connection conn = null;
                try {
                    conn = DBConn.getConnection();
                    String selectQuery = "SELECT * FROM hostels WHERE hostel_id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
                    preparedStatement.setInt(1, hostelId);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String hostelName = resultSet.getString("hostel_name");
                        System.out.println("hostel name =" + hostelName);
                        String location = resultSet.getString("location");
                        int totalRooms = resultSet.getInt("total_rooms");
                        String roomType = resultSet.getString("room_type");
                        int occupied = resultSet.getInt("occupied");
                        double price = resultSet.getDouble("price");
                        String amenitiesString = resultSet.getString("amenities");
                        String documentPath = resultSet.getString("documents");
                        String address = resultSet.getString("address");
                        String landmark = resultSet.getString("landmark");
                        String gender=resultSet.getString("gender");

                        // Create a Hostel object or use a Map to store the data
                        Hostel hostel = new Hostel(hostelName, location, totalRooms, roomType, occupied, price,
                                amenitiesString, documentPath,address,landmark,gender);

                        // Convert the Hostel object to JSON using Jackson ObjectMapper
                        ObjectMapper objectMapper = new ObjectMapper();
                        String hostelJson = objectMapper.writeValueAsString(hostel);

                        // Send the JSON response
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(hostelJson);
                    } else {
                        response.getWriter().write("{\"error\": \"No data found for hostelId: " + hostelId + "\"}");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().write("{\"error\": \"Database error occurred while fetching data.\"}");
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } catch (NumberFormatException e) {
                System.err.println("Invalid hostelId format: " + hostelIdToPrefill);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            System.err.println("Hostel id is missing or empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}