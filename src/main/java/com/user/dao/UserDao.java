package com.user.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import com.user.model.User;

public class UserDao {
	
	private String URL="jdbc:mysql://localhost:3306/Movie_Ticket_Booking_System";
	
    private String USERNAME = "root"; 
    private String PASSWORD = "Nitin@1513";
    
    private static final String INSERT_USER_SQL="INSERT INTO users"+"(uname,email,country,password) VALUES "+"(?,?,?,?);";
    private static final String SELECT_USER_BY_ID="SELECT * FROM UDERS WHERE ID=?;";
    private static final String  SELECT_ALL_USERS="SELECT * FROM USERS;";
    private static final String DELETE_USER_SQL="DELETE FROM USERS WHERE ID=?;";
    private static final String UPADATE_USERS_SQL="UPDATE USERS SET UNAME=?, EMAIL=?, COUNTRY=?, PASSWORD=?, WHERE ID=?;";
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
    
    
    public void insertUser(User user) {
    	UserDao dao=new UserDao();
    	try(Connection connection=dao.getConnection()){
    		
    		PreparedStatement preparedstatement=connection.prepareStatement(INSERT_USER_SQL);
            preparedstatement.setString(1,user.getName());
            preparedstatement.setString(2,user.getEmail());
            preparedstatement.setString(3,user.getCountry());
            preparedstatement.setString(4,user.getPassword());
            
            preparedstatement.executeUpdate();
    		
    		
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		
    	}
    	
    	
    }
    public User selectuser(int id) {
    	User user=new User();
    	UserDao dao=new UserDao();
    	try (Connection connection=dao.getConnection()){
    		PreparedStatement preparedstatement=connection.prepareStatement(SELECT_USER_BY_ID);
    		preparedstatement.setInt(1,id);
    		
    		ResultSet resultset=preparedstatement.executeQuery();
    		while (resultset.next()) {
    			user.setId(id);
    			user.setName(resultset.getString("uname"));
    			user.setEmail(resultset.getString("email"));
    			user.setCountry(resultset.getString("country"));
    			user.setPassword(resultset.getString("password"));
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		
    	}
    	return user;
    }
    
    public List<User> selectAllUsers(){
    	List<User> users=new ArrayList<User>();
    	UserDao  dao=new UserDao();
    	try (Connection connection=dao.getConnection()){
    		PreparedStatement preparedstatement=connection.prepareStatement(SELECT_ALL_USERS);
    		ResultSet resultset=preparedstatement.executeQuery();
    		while (resultset.next()) {
    			int id=resultset.getInt("id");
    			String uname=resultset.getString("uname");
    			String email=resultset.getString("email");
    			String country=resultset.getString("country");
    			String password=resultset.getString("password");
    			
    			users.add(new User(id,uname,email,country,password));
    			
    		}
    	}
    	catch(Exception e) {
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
    
    
}
