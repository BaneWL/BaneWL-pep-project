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
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generatedAccountId = rs.getInt("account_id");
                return new Account(generatedAccountId, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Account newAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return newAccount;
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public Message createNewMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generatedMessageId = rs.getInt("message_id");
                return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return messages;
    }

    public Message getMessageGivenId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public Message deleteMessageGivenId(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            sql = "DELETE FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
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
        return null;
    }

    public Message updateMessageGivenId(int id, String newMessageText){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            sql = "SELECT * FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
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
        return null;
    }

    public List<Message> getAllMessagesGivenId(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        return messages;
    }
}
