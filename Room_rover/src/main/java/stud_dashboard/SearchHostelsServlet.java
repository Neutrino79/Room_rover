package stud_dashboard;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
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

@WebServlet("/search")
public class SearchHostelsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Call the searchHostels method for processing
        searchHostels(request, response);
    }

    private void searchHostels(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;
        PreparedStatement preparedStatement = null;
        
        try {
            conn = DBConn.getConnection();

            // Base query to retrieve matching hostels based on user input
            String searchQuery = "SELECT * FROM hostels WHERE status = 'live'";

            // Add conditions based on user input (modify as needed)
            String hostelName = request.getParameter("searchInput");
            String location = request.getParameter("location");
            String priceRange = request.getParameter("price");
            System.out.println("name="+hostelName);
            System.out.println("loc="+location);
            System.out.println("price ="+priceRange);

            // Set parameters based on user input
            int parameterIndex = 1;

            if (hostelName != null && !hostelName.isEmpty()) {
                searchQuery += " AND hostel_name LIKE ?";
            }

            if (location != null && !location.isEmpty()) {
                searchQuery += " AND location = ?";
            }
            if (priceRange != null && !priceRange.isEmpty()) {
                // Split the price range into minimum and maximum values
                String[] priceLimits = priceRange.split("-");
                minPrice = Double.parseDouble(priceLimits[0]);
                maxPrice = (priceLimits.length > 1) ? Double.parseDouble(priceLimits[1]) : Double.MAX_VALUE;
                searchQuery += " AND price BETWEEN ? AND ?";
            }

            // Prepare the statement with the dynamically built query
             preparedStatement = conn.prepareStatement(searchQuery);

            // Set parameters based on user input
            if (hostelName != null && !hostelName.isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + hostelName + "%"); // Using LIKE for partial matches
            }
            if (location != null && !location.isEmpty()) {
                preparedStatement.setString(parameterIndex++, location);
            }
            if (priceRange != null && !priceRange.isEmpty()) {
                preparedStatement.setDouble(parameterIndex++, minPrice);
                preparedStatement.setDouble(parameterIndex++, maxPrice);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set and build JSON array
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (resultSet.next()) {
                // Add hostel details to the JSON array
                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("hostelName", resultSet.getString("hostel_name"))
                        .add("location", resultSet.getString("location"))
                        .add("price", resultSet.getDouble("price"))
                        // Add more properties as needed
                );
            }

            // Build the final JSON array
            JsonArray jsonArray = jsonArrayBuilder.build();

            // Send JSON response to the client
            out.print(jsonArray);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching hostels");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}