package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangeStatusServlet1 extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hostelId = request.getParameter("hostelId");
        String newStatus = "live";
        
        HostelManagementService.changeHostelStatus(hostelId,newStatus);
        response.sendRedirect("adminPanel.jsp");
    }
}

