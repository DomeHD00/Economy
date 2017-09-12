package net.twerion.economy.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import net.twerion.economy.EconomyAPI;
import net.twerion.economy.Main;


public class PlayerJoin implements Listener {

	public static Plugin plugin;
	
	public PlayerJoin(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void Player(PlayerJoinEvent e) {
		Main.getInstance().getPlayerAccount().put(e.getPlayer(), new EconomyAPI(e.getPlayer()));
	}

}
