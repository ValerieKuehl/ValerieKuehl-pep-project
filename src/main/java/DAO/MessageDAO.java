package DAO;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import Util.ConnectionUtil;
import Model.Message;

public class MessageDAO {
    // Add a new message into the database
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(
                        generated_message_id, 
                        message.getPosted_by(), 
                        message.getMessage_text(), 
                        message.getTime_posted_epoch());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Get a List of all messages
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
 
    // Get a message based on its message_id
    public Message getMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message returnMessage = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return returnMessage;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Delete a message based on its message_id
    public void deleteMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);

            ps.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // Edit a message based on an ID
    public void patchMessageByID(int message_id, String new_message_text){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, new_message_text);
            ps.setInt(2, message_id);

            ps.executeUpdate();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // Get all messages of a certain account
    public List<Message> getAllMessagesByAccountID(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
