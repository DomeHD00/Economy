package net.twerion.economy.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.twerion.economy.Main;





public class EconomyAPI {

	Player p;
    HashMap<String, Double> account = new HashMap<String, Double>();
    HashMap<String, Integer> transaktionen = new HashMap<String, Integer>();
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
							transaktionen.put(mysql.getString("accountId"), mysql.getInt("transaktionen"));
							accountNames.add(mysql.getString("accountId"));
							updateScoreboard();
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

					Mysql.update("INSERT INTO account VALUES('" + p.getUniqueId() + "','" + p.getName() + "','"+ newAccountId + "', '10', '0')");
					accountNames.add(newAccountId);
					account.put(newAccountId, 10.0);
					transaktionen.put(newAccountId, 0);
					updateScoreboard();
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
						updateScoreboard();
					}
				} catch (SQLException e) {
					System.out.println(e.getErrorCode());
				}
			}
		});
	}
	
	public void displayAllAccount() {
			p.sendMessage(Main.getInstance().getPrefix() + "§aDu hast folgende accounts: §e" + account.keySet());
	}
	public void displayMoney(String account) {
		p.sendMessage(Main.getInstance().getPrefix() + "§aauf dem Konto §e" + account + " §asind§e " + this.account.get(account) + " §amoney");
    }
	public void displayLastTransaktionen(final String account) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				ResultSet mysql = Mysql.getResult("SELECT * FROM transaktionen WHERE account = '" + account + "' ORDER BY id DESC LIMIT 10 ");
				try {
					while(mysql.next()){
						if(mysql.getString("action").equals("add")){
							p.sendMessage(Main.getInstance().getPrefix() + "§a" + mysql.getDouble("money") + " §7| §e" + mysql.getString("commit"));
						}else {
							p.sendMessage(Main.getInstance().getPrefix() + "§c" + mysql.getDouble("money") + " §7| §e" + mysql.getString("commit"));
						}
						
					}
				} catch (SQLException e) {
					System.out.println(e.getErrorCode());
				}	
			}
		});

    }
	public void addCoins(final String account,final double i, final String commit){
		final double ni =  this.account.get(account)+i;
		final int t = transaktionen.get(account) + 1;
		
		this.account.remove(account);
		this.account.put(account, ni);
		this.transaktionen.remove(account);
		this.transaktionen.put(account, t);
		
		updateScoreboard();
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {

				Mysql.update("update account set money = '"+ ni +"' where accountId = '"+ account +"'");
				Mysql.update("update account set transaktionen = '"+ t +"' where accountId = '"+ account +"'");
				Mysql.update("INSERT INTO transaktionen (account, action, money, commit) VALUES('" + account + "','add','"+ i + "', '"+ commit +"')");
			}
		});
		
	}
	
	public void removeCoins(final String account,final double i,final String commit){
		if (!hasAccount()) return;
		final double ni = this.account.get(account) - i;
		final int t = transaktionen.get(account) + 1;
		if(ni <= 0){
			this.account.remove(account);
			this.account.put(account, (double) 0);
			updateScoreboard();
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
				public void run() {
					Mysql.update("update account set money = '"+ 0 +"' where accountId = '"+ account +"'");
					Mysql.update("update account set transaktionen = '"+ t +"' where accountId = '"+ account +"'");
					Mysql.update("INSERT INTO transaktionen (account, action, money, commit) VALUES('" + account + "','remove','"+ i + "', '"+ commit +"'");
					
				}
			});
		
			return;
		}
		this.account.remove(account);
		this.account.put(account, ni);
		this.transaktionen.remove(account);
		this.transaktionen.put(account, t);
		updateScoreboard();
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			public void run() {
				
				Mysql.update("update account set money = '"+ ni +"' where accountId = '"+ account +"'");
				Mysql.update("update account set transaktionen = '"+ t +"' where accountId = '"+ account +"'");
				Mysql.update("INSERT INTO transaktionen (account, action, money, commit) VALUES('" + account + "','remove','"+ i + "', '"+ commit +"')");
			}
		});

	}
	
	public double getCoinsFormAccount(String account){
		if (!hasAccount()) return 0.0;
		return this.account.get(account);
	}
	public int getTransaktionenFormAccount(String account){
		if (!hasAccount()) return 0;
		return transaktionen.get(account);
	}
	public double getAllMoney(){
		if (!hasAccount()) return 0;
		double m = 0;
		for(String account : accountNames){
			m = m + getCoinsFormAccount(account);
		}
		return m;
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
	public void setScoreboard(){
		new ScoreboardAPI().sendScoreboard(p);
	}
	public void updateScoreboard(){
		new ScoreboardAPI().updateScoreboard(p);
	}

	public void sendMoneyToOtherAccont(final Player sender,final String taget, final String accountSender,final String accountTaget,final double i) {

		if (sender.equals(taget)) {
			removeCoins(accountSender, i , "Transfer to other Accont");
			addCoins(accountTaget, i, "Transfer to other Accont");
			updateScoreboard();
		} else {
			Player pTaget = Bukkit.getPlayer(taget);
			if (pTaget != null) {
				removeCoins(accountSender, i, "Transfer to other Accont");
				Main.getInstance().getPlayerAccount().get(pTaget).addCoins(accountTaget, i, "Transfer to other Accont");
				pTaget.sendMessage(Main.getInstance().getPrefix() + "§aDir wurden §e" + i + " §aMoney überwisen");
				updateScoreboard();
			} else {
				removeCoins(accountSender, i, "Transfer to other Accont");
				Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
					public void run() {
						double tagetMoney = i;
							try {
							ResultSet mysql = Mysql.getResult("SELECT money,accountId FROM account where Name ='" + taget + "'");
								while (mysql.next()) {
									if(mysql.getString("accountId").equals(accountTaget)){
										tagetMoney = i + mysql.getDouble("money");
									}
								} 
							}catch (SQLException e) {
									e.printStackTrace();
							}
						
						
						try {
							Mysql.update("update account set money = '" + tagetMoney + "' where accountId = '" + accountTaget + "'");
						} catch (Exception e) {
							p.sendMessage(Main.getInstance().getPrefix() + "§cPlayer ist nicht Registriert!");
							addCoins(accountSender, i, "Transfer to other Accont canneled");
						}
					}
				});

			}
		}

	}
	
	
	 static String getNewAccountId(){
		Random r = new java.util.Random();
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		  
		return "" + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length())) + str.charAt((int)(r.nextFloat() * str.length()));
	}
	
	
}
