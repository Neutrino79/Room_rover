package registration;

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

import com.ConnInit.*;
import com.mysql.cj.xdevapi.Statement;

import java.util.Calendar;
import java.util.Date;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}
   
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String errorMessage = null;
		Connection conn = null;
        PrintWriter out = response.getWriter();
        try 
        {
        	 conn= DBConn.getConnection();
            if (conn != null) 
            {
            	out.print(conn);
                out.println("Database Connection Successful");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                String rspass = request.getParameter("re_pass");
                String connum = request.getParameter("contact");
                String userType = request.getParameter("user_type");
                String birthdt=request.getParameter("dob");
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                try 
                {
                	int rowsInserted=0;
                	
                	if("student".equals(userType))
                	{
                		ResultSet checkRs = null;
                    	PreparedStatement checkPs = null;
                		String checkQuery = "SELECT * FROM Students WHERE email = ?";
                	    checkPs = conn.prepareStatement(checkQuery);
                	    checkPs.setString(1, email);
                	    checkRs = checkPs.executeQuery();

                        if (checkRs.next()) 
                        {
                        	out.print("Email already exists. Please try a different one.");
                        }
                        else
                        {
                		    PreparedStatement ps = conn.prepareStatement("INSERT INTO Students(student_name, email, address, password, phone, aadhar, dob, gender, college_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            ps.setString(1, name);
                            ps.setString(2, email);
                            ps.setString(3, "None"); // Address
                            ps.setString(4, pass);
                            ps.setString(5, connum);
                            ps.setString(6, "None"); // Aadhar
 
                            // Convert the date format and set it as a DATE
                            java.util.Date parsedDate = inputDateFormat.parse(birthdt);
                            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                            ps.setDate(7, sqlDate);

                            ps.setString(8, "None"); // Gender
                            ps.setString(9, "None"); // College ID
                            rowsInserted = ps.executeUpdate();
                	    }
                	}
                	
                	
                	
                	else if("owner".equals(userType))
                	{
                		    String checkQuery = "SELECT * FROM HostelOwners  WHERE email = ?";
                            PreparedStatement checkPs = conn.prepareStatement(checkQuery);
                            checkPs.setString(1, email);
                            ResultSet checkRs = checkPs.executeQuery();

                        if (checkRs.next()) 
                        {
                        	out.print("Email already exists. Please try a different one.");
                        }
                        else
                        {
                		    PreparedStatement ps = conn.prepareStatement("INSERT INTO HostelOwners(owner_name, email, password, contact, dob, owner_address) VALUES(?, ?, ?, ?, ?, ?)");
                            ps.setString(1, name);
                            ps.setString(2, email);
                            ps.setString(3, pass);
                            ps.setString(4, connum);
                            // Convert the date format and set it as a DATE
                            java.util.Date parsedDate = inputDateFormat.parse(birthdt);
                            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                            ps.setDate(5, sqlDate);
                            ps.setString(6, "None"); // Owner address
                            rowsInserted = ps.executeUpdate();
                        }
                	}

                	
                	
                    if (rowsInserted > 0) 
                    {
                    	  // Registration successful,
                    	out.println("\nRegistratrion successfull");
                    } 
                    else 
                    {
                        // Registration failed, set an error message
                        out.println("\nRegistration failed. Please try again.");
                    }
                    
                  
                
                }
                catch (SQLException | ParseException e) 
                {
                    e.printStackTrace();
                    out.println("\nRegistration failed due to a database error: " + e.getMessage());
                } 
            }
            else 
            {
                out.println("\nDatabase Connection Failed");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            out.println("\nAn error occurred: " + e.getMessage());
        } 
        
        finally 
        {
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
        }
	}


	private ResultSet executeQuery(String checkQuery) {
		// TODO Auto-generated method stub
		return null;
	}
}