package net.twerion.economy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;





public class EconomyAPI {

	Player p;
    HashMap<String, Double> account = new HashMap<String, Double>();
    ArrayList<String> accountNames = new ArrayList<String>();
	
	
	public EconomyAPI(final Player p){
		this.p = p;
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				try {
				ResultSet mysql = Mysql.getResult("SELECT * FROM account");
					while (mysql.next()) {
						if (mysql.getString("Name").equals(p.getName())) {
							account.put(mysql.getString("accountId"), mysql.getDouble("money"));
							accountNames.add(mysql.getString("accountId"));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	public void createNewAccount() { 
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

					Mysql.update("INSERT INTO account VALUES('" + p.getUniqueId() + "','" + p.getName() + "','"+ newAccountId + "', '10')");
					accountNames.add(newAccountId);
					account.put(newAccountId, 10.0);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});
	}
	
	
	public void deleteAccount(final String accountId){
		if (!hasAccount()) return;
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				ResultSet mysql = Mysql.getResult("SELECT * FROM account WHERE accountId = '" + accountId + "'");
				try {
					if(mysql.next()){
						Mysql.update("DELETE FROM account WHERE accountId = '"+ accountId +"'");
						account.remove(accountId);
						accountNames.remove(accountId);
					}
				} catch (SQLException e) {
					System.out.println(e.getErrorCode());
				}
			}
		});
	}
	
	public void setDisplayAllAccount() {
			p.sendMessage(Main.getInstance().getPrefix() + "§aDu hast folgende accounts: §e" + account.keySet());
	}
	public void setDisplayMoney(String account) {
		p.sendMessage(Main.getInstance().getPrefix() + "§aauf dem Konto §e" + account + " §asind§e " + this.account.get(account) + " §amoney");
    }
	public void addCoins(final String account, Double i){
		final double ni = i+= this.account.get(account);
		this.account.remove(account);
		this.account.put(account, i);
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				Mysql.update("update account set money = '"+ ni +"' where accountId = '"+ account +"'");
			}
		});
		
	}
	public void removeCoins(final String account,double i){
		if (!hasAccount()) return;
		final double ni = i=-this.account.get(account);
		this.account.remove(account);
		this.account.put(account, i);
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				Mysql.update("update account set money = '"+ ni +"' where accountId = '"+ account +"'");
			}
		});
	}
	public void setCoins(final String account,final int i){
		if (!hasAccount()) return;
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				Mysql.update("update account set money = '"+ i +"' where accountId = '"+ account +"'");
			}
		});
	}
	
	public double getCoinsFormAccount(String account){
		if (!hasAccount()) return 0.0;
		return this.account.get(account);
	}

	public String getFirstAccount() {
		if (!hasAccount()) return null;
		return accountNames.get(0);
	}
	
	public boolean hasAccount(){
		if(!accountNames.isEmpty()) return true;
		return false;
	}
	public ArrayList<String> getAccounts(){
		return accountNames;
	}
	
	
	
	 static String getNewAccountId(){
		Random r = new java.util.Random();
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		  
		return "" + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length()));
	}
	
	
}
