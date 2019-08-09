package com.woniu.action.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.IOException;
import java.sql.Connection;

public class jdbcTolls {
    
	static String driver;
	static String url;
	static String user;
	static String password;
	static {
		
		try {
			    Properties pro = new Properties();
				pro.load(jdbcTolls.class.getResourceAsStream("user.properties"));
				driver=pro.getProperty("driver");
				url=pro.getProperty("url");
				user=pro.getProperty("user");
				password=pro.getProperty("password");
				Class.forName(driver);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
     public static  Connection open() throws ClassNotFoundException, SQLException {
    		Connection conn=(Connection) DriverManager.getConnection(url
    				,user,password);
    		 return  conn;  
     }
     public static void close(ResultSet rs,Statement stat,Connection conn) {
    		try {
    			if(rs!=null) {
    				rs.close();
    			}
    			if(stat!=null) {
    				stat.close();
    			}
    			if(conn!=null) {
    				conn.close();
    			}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
     }
}
