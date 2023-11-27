package stud_dashboard;

import com.ConnInit.DBConn;
import jakarta.json.Json;
import jakarta.json.JsonArray;
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
import java.util.ArrayList;
import java.util.List;
import jakarta.json.JsonArrayBuilder;

@WebServlet("/GetLocations")
public class GetLocationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        try {
            conn = DBConn.getConnection();

            // Execute query to retrieve live locations from the hostels table
            String query = "SELECT DISTINCT location FROM hostels WHERE status = 'live'";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set and extract locations
            List<String> locations = new ArrayList<>();
            while (resultSet.next()) {
                locations.add(resultSet.getString("location"));
            }

            // Convert locations to JSON
            String json = "{\"locations\":[";
            for (int i = 0; i < locations.size(); i++) {
                json += "\"" + locations.get(i) + "\"";
                if (i < locations.size() - 1) {
                    json += ",";
                }
            }
            json += "]}";

            // Send JSON response to the client
            out.print(json);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching locations");
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