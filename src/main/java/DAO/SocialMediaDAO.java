package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_acc_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_acc_id, account.getUsername(), account.getPassword());
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
        return null;
    }

    public List<Message> getAllMessages(){
        return null;
    }

    public Message getMessageGivenId(int id){
        return null;
    }

    public Message deleteMessageGivenId(int id){
        return null;
    }

    public Message updateMessageGivenId(int id){
        return null;
    }

    public List<Message> getAllMessagesGivenId(int id){
        return null;
    }
}
