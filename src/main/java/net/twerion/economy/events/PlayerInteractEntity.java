package net.twerion.economy.events;

import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.twerion.economy.Main;
import net.twerion.economy.config.ConfigCreator;


public class PlayerInteractEntity implements Listener{

	static double food;
	
	static {
		food = ConfigCreator.cfg.getDouble("foodCoins");
	}
	
	public PlayerInteractEntity(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public static void playerInteract(PlayerInteractEntityEvent e){
		
		if(e.getRightClicked() instanceof Animals){
			Entity en = e.getRightClicked();
			if(en instanceof Cow || en instanceof Sheep){
				if(e.getPlayer().getItemInHand().getType() == Material.WHEAT){
					giveMoneyForFood(e.getPlayer());
				}
			}else if(en instanceof Rabbit){
				if(e.getPlayer().getItemInHand().getType() == Material.CARROT){
					giveMoneyForFood(e.getPlayer());
				}
			}else if(en instanceof Chicken){
				if(e.getPlayer().getItemInHand().getType() == Material.SEEDS){
					giveMoneyForFood(e.getPlayer());
				}
			}
			
		}
		
	}
	
	
	private static void giveMoneyForFood(Player p){
		Main.getInstance().getPlayerAccount().get(p).addCoins(Main.getInstance().getPlayerAccount().get(p).getFirstAccount(), food, "Animal Food");
	}
}
