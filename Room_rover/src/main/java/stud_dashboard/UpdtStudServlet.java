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
import java.sql.SQLException;

import com.ConnInit.DBConn;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/UpdateStudDetailsServlet")
public class UpdtStudServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdtStudServlet() {
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
        int ID = (int) session.getAttribute("ID");

        // Collect data from the AJAX request
        String studaddr = request.getParameter("studaddr");
        String aadhar = request.getParameter("aadhar");
        String gender = request.getParameter("gender");
        String collegeid = request.getParameter("collegeid");

        // Update the Students table in the database
        Connection conn = null;
        try {
            conn = DBConn.getConnection();
            String query = "UPDATE Students SET address=?, aadhar=?, gender=?, college_id=? ,pf_complete=? WHERE student_id=?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, studaddr);
                preparedStatement.setString(2, aadhar);
                preparedStatement.setString(3, gender);
                preparedStatement.setString(4, collegeid);
                preparedStatement.setInt(5, 1);
                preparedStatement.setInt(6, ID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Updated successfully
                    out.println("1");
                } else {
                    // Not updated
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
