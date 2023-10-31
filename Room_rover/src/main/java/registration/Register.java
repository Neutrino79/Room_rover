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
import java.sql.SQLException;
import com.ConnInit.*;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}
   
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		Connection conn = null;
        PrintWriter out = response.getWriter();
        try 
        {
        	 conn= DBConn.getConnection();
            if (conn != null) 
            {
            	
                out.println("Database Connection Successful");
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
    

        /*
         * String sname = request.getParameter("name");
        String semail = request.getParameter("email");
        String spass = request.getParameter("pass");
        String rspass = request.getParameter("re_pass");
        String sconnum = request.getParameter("contact");
       

        try 
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO stud(sname, semail, spass, scontact) VALUES(?,?,?,?)");
            ps.setString(1, sname);
            ps.setString(2, semail);
            ps.setString(3, spass);
            ps.setString(4, sconnum);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) 
            {
                // Registration is successful, redirect to the login page with a success message
                response.sendRedirect("login.jsp?registrationSuccess=1");
            } 
            else 
            {
                // Registration failed, set an error message
                out.println("Registration failed. Please try again.");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            out.println("Registration failed due to a database error. Please try again later.");
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
        */
	}

}
