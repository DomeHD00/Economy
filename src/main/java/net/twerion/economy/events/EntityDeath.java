package net.twerion.economy.events;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import net.twerion.economy.Main;
import net.twerion.economy.config.ConfigCreator;

public class EntityDeath implements Listener {

	public EntityDeath(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public static void InteractEntityEvent(EntityDeathEvent e) {

		if (e.getEntity().getKiller() != null || e.getEntity().getKiller() instanceof Player) {
			if (e.getEntity() instanceof Monster) {
				giveMoneyForMonster(e.getEntity().getKiller());
			}
			
			if (e.getEntity() instanceof Animals) {
				removeMoneyForAnimals(e.getEntity().getKiller());
			}
		}
	}

	private static void removeMoneyForAnimals(Player p) {
		Main.getInstance().getPlayerAccount().get(p).removeCoins(Main.getInstance().getPlayerAccount().get(p).getFirstAccount(), ConfigCreator.cfg.getDouble("animalsFoodCoins"));
	}

	private static void giveMoneyForMonster(Player p) {
		Main.getInstance().getPlayerAccount().get(p).addCoins(Main.getInstance().getPlayerAccount().get(p).getFirstAccount(), ConfigCreator.cfg.getDouble("monsterKillCoins"));
	}
}
