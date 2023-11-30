package login;

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
import jakarta.servlet.http.HttpSession;


import com.ConnInit.DBConn;

@WebServlet("/login")
public class login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Connection conn = null;
        
        PrintWriter out = response.getWriter();
        try {
        	
            conn = DBConn.getConnection();

            if (conn != null) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String role = request.getParameter("role");

                try {
                	String IDcol;
                    
                    String userTable;

                    if ("student".equals(role)) 
                    {
                        userTable = "Students";
                        IDcol="student_id";
                    } 
                    else if ("owner".equals(role)) 
                    {
                        userTable = "HostelOwners";
                        IDcol="owner_id";
                    }
                    else 
                    {
                        // Handle invalid role
                        out.println("Invalid role");
                        return;
                    }
                    String selectQuery = "SELECT * FROM ";
                    selectQuery += userTable + " WHERE email = ?";

                    PreparedStatement selectPs = conn.prepareStatement(selectQuery);
                    selectPs.setString(1, email);

                    ResultSet checkRs = selectPs.executeQuery();

                    if (checkRs.next()) {
                        // User email exists in the database, now check the password
                        selectQuery = "SELECT "+IDcol+" FROM " + userTable + " WHERE email = ? AND password = ?";
                        selectPs = conn.prepareStatement(selectQuery);
                        selectPs.setString(1, email);
                        selectPs.setString(2, password);
                        
                        checkRs = selectPs.executeQuery();
                        
                        
                        if (checkRs.next()) {
                            // Login is successful
                        	int ID = checkRs.getInt(IDcol);
                            out.println("Login Successful");
                            out.println("<script>showSuccessAnimation();</script>");// JavaScript to show success animation
                            HttpSession session = request.getSession();
                            session.setAttribute("email", email);
                            if ("student".equals(role)) 
                            {
                            	session.setAttribute("StudID", ID);

                            } 
                            else 
                            {
                            session.setAttribute("ID", ID);
                            }

                            if("student".equals(role))
                            		{
                            			out.println("<script>window.location.replace('/Room_rover/stud_dashboard.html');</script>");
                            		}
                            else if("owner".equals(role))
                            {
                            	out.println("<script>window.location.replace('/Room_rover/owner_dashboard.html');</script>");
                            }
                        } else {
                            // Login failed
                            out.println("Invalid email or password");
                            out.println("<script>showFailureAnimation();</script>"); // JavaScript to show failure animation
                        }
                    } else {
                        out.println("Email doesn't exist in the database");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    out.println("Login failed due to a database error: " + e.getMessage());
                    
                   // response.sendRedirect("/Room_rover/Login.html");
                }
            } else {
                out.println("Database Connection Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
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
