package com.user.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import com.util.Constants;
import com.user.model.User;

public class UserDao {
	
	private String URL = Constants.DB_URL;
	private String USERNAME = Constants.DB_USERNAME;
	private String PASSWORD = Constants.DB_PASSWORD;
    
    private static final String INSERT_USER_SQL = 
        "INSERT INTO users (username, name, email, country, address, password, role) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
    private static final String SELECT_USER_BY_ID = 
        "SELECT user_id, username, name, email, country, address, password, role, created_at " +
        "FROM users WHERE user_id = ?";
        
    private static final String SELECT_USER_BY_EMAIL_AND_PASSWORD = 
        "SELECT user_id, username, name, email, country, address, password, role, created_at " +
        "FROM users WHERE email = ? AND password = ?";
        
    private static final String UPDATE_USERS_SQL = 
        "UPDATE users SET name=?, email=?, country=?, address=? WHERE user_id=?";
        
    private static final String SELECT_ALL_USERS = "SELECT user_id, username, name, email, country, address, password, role, created_at FROM users;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE user_id=?;";
    
	public UserDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    public Connection getConnection() {
    	Connection  connection=null;
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
    	}
    	catch(SQLException | ClassNotFoundException e) {
    		e.printStackTrace();
    		
    	}
    	catch(Exception e ) {
    		e.printStackTrace();
    		
    	}
    	return connection;
    }
    
    
    public boolean insertUser(User user) throws SQLException {
        if (!user.isValidEmail() || !user.isValidPassword()) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_USER_SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getCountry());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole() != null ? user.getRole() : "USER");
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("country"),
            rs.getString("address"),
            rs.getString("password")
        );
        user.setRole(rs.getString("role"));
        user.setCreated_at(rs.getTimestamp("created_at"));
        return user;
    }

    public User selectuser(int userId) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_USER_BY_ID)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_USERS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean deleteuser(int id)
    {
    	boolean status=false;
    	UserDao dao=new UserDao();
    	try (Connection connection=dao.getConnection()){
    		PreparedStatement preparedstatement=connection.prepareStatement(DELETE_USER_SQL);
    		preparedstatement.setInt(1,id);
    		
    		status=preparedstatement.execute();
    		
    		
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		
    	}
    	return status;
    	
    }
    
    public User selectUserByEmailAndPassword(String email, String password) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_AND_PASSWORD);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            
            System.out.println("Executing query for email: " + email); // Debug log
            
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = mapResultSetToUser(resultSet);
                System.out.println("User found: " + user.getUsername()); // Debug log
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage()); // Debug log
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    
    public boolean updateUser(User user) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_USERS_SQL)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getCountry());
            stmt.setString(4, user.getAddress());
            stmt.setInt(5, user.getUser_id());
            return stmt.executeUpdate() > 0;
        }
    }
}
