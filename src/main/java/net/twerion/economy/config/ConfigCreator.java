package net.twerion.economy.config;


public class ConfigCreator {

	public ConfigManager manager;
	public static ConfigHandler cfg;
	public static ConfigHandler database;

	public void createConfig() {
		this.manager = new ConfigManager();
		ConfigCreator.cfg = this.manager.getNewConfig("config");
		if (ConfigCreator.cfg.isFirstrun()) {

			ConfigCreator.cfg.set("interest", 5, "Zinsen in %");
			ConfigCreator.cfg.set("foodCoins", 3.5, "Die Coins die man fürs füttern bekommen!");
			ConfigCreator.cfg.set("monsterKillCoins", 3.5, "Die Coins die man fürs töten von Monsteren bekommt!");
			ConfigCreator.cfg.set("animalsFoodCoins", 3.5, "Die Coins die man verliert fürs füttern von Tieren!");
			ConfigCreator.cfg.set("beefFoodCoins", 3.5, "Die Coins die man verliert fürs essen von Fleisch!");
			
			ConfigCreator.cfg.saveConfig();
		}
	}

	public void createDatabase() {
		this.manager = new ConfigManager();
		ConfigCreator.database = this.manager.getNewConfig("database");
		if (ConfigCreator.database.isFirstrun()) {

			ConfigCreator.database.set("host", "localhost", "Database settings");
			ConfigCreator.database.set("port", 3306);
			ConfigCreator.database.set("user", "user");
			ConfigCreator.database.set("password", "password");
			ConfigCreator.database.set("database", "database");

			ConfigCreator.database.saveConfig();
		}
	}

}
