<%@ page import="java.util.List" %>
<%@ page import="admin.hostel" %>
<%@ page import="admin.HostelManagementService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Panel - Hostel Management System</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h1>Admin Panel - Hostel Management System</h1>

    <h2>Hostel List</h2>
    <table>
        <tr>
            <th>Hostel ID</th>
            <th>Hostel Name</th>
            <th>Location</th>
            <th>Total Rooms</th>
            <th>Room Type</th>
            <th>Occupied</th>
            <th>Is Full</th>
            <th>Documents</th>
            <th>Amenities</th>
            <th>Price</th>
            <th>Status</th>
            <th>Address</th>
            <th>Landmark</th>
            <th>Gender</th>
            <th>Action</th>
        </tr>
        <%
            List<hostel> hostelList = HostelManagementService.getHostelList();
            for (hostel hostel : hostelList) {
        %>
            <tr>
                <td><%= hostel.getHostelId() %></td>
                <td><%= hostel.getHostelName() %></td>
                <td><%= hostel.getLocation() %></td>
                <td><%= hostel.getTotalRooms() %></td>
                <td><%= hostel.getRoomType() %></td>
                <td><%= hostel.getOccupied() %></td>
                <td><%= hostel.getIsFull() %></td>
                <td><%= hostel.getDocuments() %></td>
                <td><%= hostel.getAmenities() %></td>
                <td><%= hostel.getPrice() %></td>
                <td><%= hostel.getStatus() %></td>
                <td><%= hostel.getAddress() %></td>
                <td><%= hostel.getLandmark() %></td>
                <td><%= hostel.getGender() %></td>
                <td>
                    <%
                        String currentStatus = hostel.getStatus();
                        if ("pending".equals(currentStatus)) {
                    %>
                            <a href="ChangeStatusServlet1?hostelId=<%= hostel.getHostelId() %>&newStatus=accepted">Accept</a>
                            <a href="ChangeStatusServlet1?hostelId=<%= hostel.getHostelId() %>&newStatus=rejected">Reject</a>
                    <%
                        }
                    %>
                </td>
            </tr>
        <%
            }
        %>
    </table>
</body>
</html>
