package stud_dashboard;

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
import java.util.HashMap;
import java.util.Map;

import com.ConnInit.DBConn;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/FetchStuddtls")
public class fetchStudentDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public fetchStudentDetails() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        int ID = (int) session.getAttribute("StudID");
 

        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String query = "SELECT * FROM Students WHERE student_id=?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, ID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Process the result set
                    Map<String, String> studentDetails = new HashMap<>();
                    if (resultSet.next()) {
                        studentDetails.put("student_name", resultSet.getString("student_name"));
                        studentDetails.put("email", resultSet.getString("email"));
                        studentDetails.put("addr", resultSet.getString("address"));
                        studentDetails.put("phone", resultSet.getString("phone"));
                        studentDetails.put("aadhar", resultSet.getString("aadhar"));
                        studentDetails.put("dob", resultSet.getString("dob"));
                        studentDetails.put("gender", resultSet.getString("gender"));
                        studentDetails.put("college_id", resultSet.getString("college_id"));
                        studentDetails.put("pf_complete", resultSet.getString("pf_complete"));
                    }

                    // Convert the map to JSON and write it to the response
                    out.println(new ObjectMapper().writeValueAsString(studentDetails));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: Exception occurred while fetching student details. Please check your code and logs for more details. Error Message: " + e.getMessage());
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
