package net.twerion.economy.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import net.twerion.economy.Main;

public class PlayerQuit implements Listener {

	public static Plugin plugin;
	
	public PlayerQuit(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void Player(PlayerQuitEvent e){
		Main.getInstance().getPlayerAccount().remove(e.getPlayer());
	}

}
