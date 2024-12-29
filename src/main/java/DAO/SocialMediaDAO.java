package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDAO {
    public Account userRegistration(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Inserts new account entry
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            // Returns resulting row
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generatedAccountId = rs.getInt("account_id");
                return new Account(generatedAccountId, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Registration failed
        return null;
    }

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Selects row corresponding to given account
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            // Returns resulting row
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Account newAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return newAccount;
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Login failed
        return null;
    }

    public Message createNewMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Inserts new message entry
            String sql = "INSERT INTO message (posted_by, message_text) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.executeUpdate();
            // Returns resulting row
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generatedMessageId = rs.getInt("message_id");
                return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Insertion failed
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Selects all messages
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // Prepares list of resulting rows
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Return list of messages
        return messages;
    }

    public Message getMessageGivenId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Select row corresponding to given message_id
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            // Returns resulting row
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Message not found
        return null;
    }

    public Message deleteMessageGivenId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Preemptively select to be deleted row for purposes of returning
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            // Delete row corresponding to given message_id
            sql = "DELETE FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            // Return row prior to deletion
            if(rs.next()){
                int deletedMessageId = rs.getInt("message_id");
                int deletedPostedBy = rs.getInt("posted_by");
                String deletedMessageText = rs.getString("message_text");
                long deletedTimePostedEpoch = rs.getLong("time_posted_epoch");
                return new Message(deletedMessageId, deletedPostedBy, deletedMessageText, deletedTimePostedEpoch);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Deletion failed
        return null;
    }

    public Message updateMessageGivenId(int id, String newMessageText){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Update row corresponding to given message_id, using given message_text
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            // Select updated row
            sql = "SELECT * FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            // Return updated row
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int updatedMessageId = rs.getInt("message_id");
                int updatedPostedBy = rs.getInt("posted_by");
                String updatedMessageText = rs.getString("message_text");
                long updatedTimePostedEpoch = rs.getLong("time_posted_epoch");
                return new Message(updatedMessageId, updatedPostedBy, updatedMessageText, updatedTimePostedEpoch);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Update failed
        return null;
    }

    public List<Message> getAllMessagesGivenId(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // Select all messages corresponding to posted_by
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            // Prepare list of messages
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        // Return list of messages
        return messages;
    }
}
