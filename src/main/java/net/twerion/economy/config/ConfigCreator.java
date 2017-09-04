package net.twerion.economy.config;


public class ConfigCreator {

	public ConfigManager manager;
	public static ConfigHandler cfg;
	public static ConfigHandler database;

	public void createConfig() {
		this.manager = new ConfigManager();
		ConfigCreator.cfg = this.manager.getNewConfig("config");
		if (ConfigCreator.cfg.isFirstrun()) {


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
