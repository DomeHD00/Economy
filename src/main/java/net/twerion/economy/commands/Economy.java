package net.twerion.economy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.twerion.economy.EconomyAPI;
import net.twerion.economy.Main;

public class Economy implements CommandExecutor {

	private Main plugin;

	public Economy(Main Instance) {
		this.plugin = Main.getInstance();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("economy")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage("§cNur Spieler dürfen /economy nutzen");
				return true;
			}
			Player p = (Player) sender;

			if (args.length == 1) {
				if (args[0].equals("create")) {
					EconomyAPI.createNewAccount(p);
					p.sendMessage(Main.getInstance().getPrefix() + "§aDu hast nun ein neues Konto");
				}else if(args[0].equals("money")){
					EconomyAPI.setDisplayAllAccount(p);
				}
			}else if(args.length == 2){
				if (args[0].equals("delete")) {
					EconomyAPI.deleteAccount(args[1]);
					p.sendMessage(Main.getInstance().getPrefix() + "§aDu hast das Konto §e" + args[1] + " §aGelöscht");
				}
			}

		}
		return true;

	}

}
