package net.twerion.economy;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.twerion.economy.commands.Economy;
import net.twerion.economy.config.ConfigCreator;
import net.twerion.economy.events.EntityDeath;
import net.twerion.economy.events.PlayerInteract;
import net.twerion.economy.events.PlayerInteractEntity;
import net.twerion.economy.events.PlayerItemConsume;
import net.twerion.economy.events.PlayerJoin;
import net.twerion.economy.events.PlayerQuit;


public class Main extends JavaPlugin{

	
	String prefix = "§7[§cEconomySystem§7]:";
	String noPermission = "§cDafür hast du keine Rechte!";
	static Main instance;
	ConfigCreator configCreator = new ConfigCreator();
	HashMap<Player, EconomyAPI> playerAccount = new HashMap<Player, EconomyAPI>();
	
	@Override
	public void onEnable() {
		super.onEnable();
		instance = this;
		
		//Create Configs
		configCreator.createDatabase();
		configCreator.createConfig();
		//commands and evnts
		onCommands();
		onEvents();
		//MySQL
		Mysql.connect();
		Mysql.update("CREATE TABLE IF NOT EXISTS account(UUID VARCHAR(64),Name VARCHAR(16),accountId VARCHAR(8),money DOUBLE,transaktionen INT(4))");
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
			
			for(Player all : Bukkit.getOnlinePlayers()){
				for(String account : playerAccount.get(all).getAccounts()){
					playerAccount.get(all).addCoins(account, playerAccount.get(all).getCoinsFormAccount(account) * ConfigCreator.cfg.getInt("interest") / 100);
				}
			}
			
			
			}
		}, 0, 20 * 60 * 5); //20 * 60 * 5
	}
	
	private void onCommands(){
		getCommand("economy").setExecutor(new Economy(this));
	}
	private void onEvents(){
		new PlayerJoin(this);
		new PlayerQuit(this);
		new PlayerInteractEntity(this);
		new EntityDeath(this);
		new PlayerItemConsume(this);
		new PlayerInteract(this);
	}
	public static Main getInstance(){
		return instance;
	}
	public String getPrefix(){
		return prefix;
	}
	public String getNoPermission(){
		return noPermission;
	}
	public HashMap<Player, EconomyAPI> getPlayerAccount(){
		return playerAccount;
	}
	
}
