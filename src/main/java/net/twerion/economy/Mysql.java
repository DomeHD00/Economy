package net.twerion.economy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.twerion.economy.config.ConfigCreator;



public class Mysql {

	public static String host = ConfigCreator.database.getString("host");
	public static String port = ConfigCreator.database.getString("port");
	public static String username = ConfigCreator.database.getString("user");
	public static String password = ConfigCreator.database.getString("password");
	public static String database = ConfigCreator.database.getString("database");
	public static Connection con;
	
	
	public static void connect(){ 
		if(!isConnect()){
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,username,password);
				System.out.println("MySQL Verbidung Augebaut!");
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static void disconnect(){
		if(!isConnect()){
			try {
				con.close();
				System.out.println("MYSQL Verbunden geschlossen!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isConnect(){
		return (con == null ? false:true);
	}
	
	public static void update(String qry){
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ResultSet getResult(String qry){
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
