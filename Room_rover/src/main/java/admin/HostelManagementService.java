package admin;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.ConnInit.DBConn;
public class HostelManagementService
{

    private static Connection connection;
    
    public static   List<feedback> getFeedbackList()
    {
    	 List<feedback> feedbackList = new ArrayList<>();
    	 Connection conn = null;
    	 conn = DBConn.getConnection();
         try 
         {
        	 conn = DBConn.getConnection();
             String query = "SELECT * FROM feedback";
             try (PreparedStatement preparedStatement = conn.prepareStatement(query)) 
             {
                 // Execute the query
                 ResultSet resultSet = preparedStatement.executeQuery();

                 // Process the result set
                 while (resultSet.next()) 
                 {
                     feedback feedback = new feedback();
                     feedback.setStudentId(resultSet.getString("student_id"));
                     feedback.setFeedbackText(resultSet.getString("feedback_text"));
                     
                     feedbackList.add(feedback);
                 }
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         } 
         finally 
         {
             // Close the connection in the finally block to ensure it's always closed
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

         return feedbackList;
     }
    public static List<hostel> getHostelList() 
    {
    	System.out.println("in the hostel management");
    	List<hostel> hostelList = new ArrayList<>();
    	 Connection conn = null;
    	 conn = DBConn.getConnection();
         try 
         {
        	 conn = DBConn.getConnection();
           
            String query = "SELECT * FROM hostels";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) 
            {
                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();
                
                // Process the result set
                while (resultSet.next()) 
                {
                    hostel hostel = new hostel();
                    hostel.setHostelId(resultSet.getInt("hostel_id"));
                    hostel.setHostelName(resultSet.getString("hostel_name"));
                    hostel.setLocation(resultSet.getString("location"));
                    hostel.setOwnerId(resultSet.getInt("owner_id"));
                    hostel.setTotalRooms(resultSet.getInt("total_rooms"));
                    hostel.setRoomType(resultSet.getString("room_type"));
                    hostel.setOccupied(resultSet.getInt("occupied"));
                    hostel.setIsFull(resultSet.getInt("Is_full"));
                    hostel.setDocuments(resultSet.getString("documents"));
                    hostel.setAmenities(resultSet.getString("amenities"));
                    hostel.setPrice(resultSet.getDouble("price"));
                    hostel.setStatus(resultSet.getString("status"));
                    hostel.setAddress(resultSet.getString("address"));
                    hostel.setLandmark(resultSet.getString("landmark"));
                    hostel.setGender(resultSet.getString("gender"));


                    hostelList.add(hostel);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            // Close the connection in the finally block to ensure it's always closed
            if (conn != null) 
            {
                try 
                {
                	conn.close();
                } catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
        }

        return hostelList;
    }


    public static void changeHostelStatus(String hostelId, String newStatus) 
    {
    	 Connection conn = null;
    	 conn = DBConn.getConnection();
         try 
         {
        	 conn = DBConn.getConnection();
           
    		 
             String query = "UPDATE hostels SET status = ? WHERE hostel_id = ?";
             try (PreparedStatement preparedStatement = conn.prepareStatement(query)) 
             {
                 preparedStatement.setString(1, newStatus);
                 preparedStatement.setString(2, hostelId);

                 // Execute the update
                 preparedStatement.executeUpdate();
             }
         } 
    	 catch (SQLException e) 
    	 {
             e.printStackTrace();
         } finally 
    	 {
             // Close the connection in the finally block to ensure it's always closed
             if (conn != null) 
             {
                 try 
                 {
                	 conn.close();
                 } catch (SQLException e) 
                 {
                     e.printStackTrace();
                 }
             }
         }
    }
	public static void changeHostelStatus(String hostelId, String hostelAdd, String newStatus) {
		// TODO Auto-generated method stub
		
	}
    }   





