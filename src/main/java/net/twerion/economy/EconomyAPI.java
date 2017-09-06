package net.twerion.economy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.base.FinalizablePhantomReference;



public class EconomyAPI {

	
	public static void createNewAccount(final Player p) { 

		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {

				try {
					String newAccountId = getNewAccountId();
					ResultSet mysql = Mysql.getResult("SELECT * FROM account WHERE accountId = '" + newAccountId + "'");
					if (mysql.next()) {
						ResultSet mysql1 = Mysql.getResult("SELECT * FROM account WHERE accountId = '" + newAccountId + "'");
						while (mysql1.next()) {
							newAccountId = getNewAccountId();
						}
					}

					Mysql.update("INSERT INTO account VALUES('" + p.getUniqueId() + "','" + p.getName() + "','"+ newAccountId + "', '0')");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
	}
	
	
	public static void deleteAccount(final String account){
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				ResultSet mysql = Mysql.getResult("SELECT * FROM account WHERE accountId = '" + account + "'");
				try {
					if(mysql.next()){
						Mysql.update("DELETE FROM account WHERE accountId = '"+ account +"'");
					}
				} catch (SQLException e) {
					System.out.println(e.getErrorCode());
				}
			}
		});
	}

	public static void setDisplayAllAccount(final Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				ResultSet mysql = Mysql.getResult("SELECT * FROM account WHERE UUID = '" + p.getUniqueId() + "'");
				
				try {
					while (mysql.next()) {
						p.sendMessage(Main.getInstance().getPrefix() + "§aKonto: " + mysql.getString("accountId") + " §eGeld: " + mysql.getInt("money"));
					}
				} catch (SQLException e) {
					System.out.println(e.getErrorCode());

				}

			}
		});
	}
	
	public static String getNewAccountId(){
		Random r = new java.util.Random();
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		  
		return "" + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length()));
	}
	
	
}
