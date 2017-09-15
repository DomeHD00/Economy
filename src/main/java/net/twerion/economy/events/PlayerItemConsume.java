package net.twerion.economy.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import net.twerion.economy.Main;
import net.twerion.economy.config.ConfigCreator;

public class PlayerItemConsume implements Listener {

	public PlayerItemConsume(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public static void playerItem(PlayerItemConsumeEvent e) {
		
		
		if(e.getItem().getType() == Material.RAW_BEEF || e.getItem().getType() == Material.COOKED_BEEF || e.getItem().getType() == Material.RAW_CHICKEN || e.getItem().getType() == Material.COOKED_CHICKEN || e.getItem().getType() == Material.RABBIT || e.getItem().getType() == Material.COOKED_RABBIT || e.getItem().getType() == Material.MUTTON || e.getItem().getType() == Material.COOKED_MUTTON){
			removeMoneyForBeefFood(e.getPlayer());
		}
	}

	private static void removeMoneyForBeefFood(Player p) {
		Main.getInstance().getPlayerAccount().get(p).removeCoins(Main.getInstance().getPlayerAccount().get(p).getFirstAccount(), ConfigCreator.cfg.getDouble("beefFoodCoins"), "Consume Item");
	}
}
