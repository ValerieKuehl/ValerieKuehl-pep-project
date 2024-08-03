package DAO;

import java.sql.*;
import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {
    // Add a new account into the database.
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);" ;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int generated_account_id = (int) rs.getLong(1);
                return new Account(
                        generated_account_id, 
                        account.getUsername(), 
                        account.getPassword());
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    // Get and return an account that has the same username and password.
    public Account getAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account returnAccount = new Account(
                        rs.getInt("account_id"), 
                        rs.getString("username"), 
                        rs.getString("password"));
                return returnAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Verify if an account with a specific username exist. Returns an account without a pasword for security purposes.
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account returnAccount = new Account(
                        rs.getString("username"),
                        "");
                return returnAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Verify if an account with a specific account_id exist. Returns an account without a pasword for security purposes.
    public Account getAccountByID(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account returnAccount = new Account(
                        rs.getString("username"),
                        "");
                return returnAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
