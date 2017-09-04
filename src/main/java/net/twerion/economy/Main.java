package net.twerion.economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.twerion.economy.config.ConfigCreator;


public class Main extends JavaPlugin{

	
	String prefix = "§7[§cEconomySystem§7]:";
	String noPermission = "§cDafür hast du keine Rechte!";
	static Main instance;
	ConfigCreator configCreator = new ConfigCreator();
	
	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		
		//Create Configs
		configCreator.createDatabase();
		configCreator.createConfig();
		//MySQL
		Mysql.connect();
		Mysql.update("CREATE TABLE IF NOT EXISTS account(UUID VARCHAR(64),Name VARCHAR(16),accountId VARCHAR(6),money INT(10))");
		isMysqlConnect();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		Mysql.disconnect();
	}
	
	@SuppressWarnings("deprecation")
	private void isMysqlConnect() {
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (!Mysql.isConnect()) {
					Mysql.connect();
				}
			}
		}, 20 * 5, 0);
	}
	
	
	public static Main getinstance(){
		return instance;
	}
	public String getPrefix(){
		return prefix;
	}
	public String getNoPermission(){
		return noPermission;
	}
	
}
