package net.twerion.economy.events;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.twerion.economy.Main;
import net.twerion.economy.utils.EconomyAPI;

public class PlayerInteract implements Listener{

	public PlayerInteract(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public static void player(PlayerInteractEvent e){
		Player p = e.getPlayer();
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.GOLD_BLOCK){
				
				Inventory inventory = p.getServer().createInventory(null, 9 * 6, "§6Konten");
				EconomyAPI ea = Main.getInstance().getPlayerAccount().get(p);
				
				for(String accounts : ea.getAccounts()){
					inventory.addItem(createIteam(accounts, ea));
				}
				p.openInventory(inventory);
			}
		}
	}

	private static ItemStack createIteam(String account, EconomyAPI ea) {
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§5Coins: §e" + ea.getCoinsFormAccount(account));
		lore.add("§5Transaktionen: §e" + ea.getTransaktionenFormAccount(account));
		//
		ItemStack item = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta meta = item.getItemMeta();
		//
		meta.setDisplayName("§6Konto: §e" + account);
		meta.setLore(lore);
		//
		item.setItemMeta(meta);
		return item;
	}
	
}
