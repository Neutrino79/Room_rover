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
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.ConnInit.DBConn;
@WebServlet("/login")
public class login extends HttpServlet 
{
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        Connection conn = null;
        PrintWriter out = response.getWriter();
        System.out.print("check");
        try 
        {
            conn = DBConn.getConnection();
            if (conn != null) 
            {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                
                try {
                	
                    String selectQuery = "SELECT * FROM ";
                    String userTable;

                    if ("student".equals(role)) 
                    {
                        userTable = "Students";
                    } 
                    else if ("owner".equals(role)) 
                    {
                        userTable = "HostelOwners";
                    } 
                    else 
                    {
                        // Handle invalid role
                        out.println("Invalid role");
                        return;
                    }

                    selectQuery += userTable + " WHERE email = ?";

                    PreparedStatement selectPs = conn.prepareStatement(selectQuery);
                    selectPs.setString(1, email);

                    ResultSet checkRs = selectPs.executeQuery();

                    if (checkRs.next()) 
                    {
                        // User email exists in the database, now check the password
                        selectQuery = "SELECT * FROM " + userTable + " WHERE email = ? AND password = ?";
                        selectPs = conn.prepareStatement(selectQuery);
                        selectPs.setString(1, email);
                        selectPs.setString(2, password);

                        checkRs = selectPs.executeQuery();

                        if (checkRs.next()) 
                        {
                            out.println("Login Successful");
                        } 
                        else 
                        {
                            out.println("Invalid email or password");
                        }
                    } 
                    else 
                    {
                        out.println("Email doesn't exist in the database");
                    }
                } 
                catch (SQLException e) 
                {
                    e.printStackTrace();
                    out.println("Login failed due to a database error: " + e.getMessage());
                }
            } 
            else 
            {
                out.println("Database Connection Failed");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            out.println("An error occurred: " + e.getMessage());
        } 
        finally 
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
    }
}