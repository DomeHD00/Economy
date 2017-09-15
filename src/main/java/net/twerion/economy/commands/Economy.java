package net.twerion.economy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.twerion.economy.Main;

public class Economy implements CommandExecutor {

	private Main plugin;

	public Economy(Main Instance) {
		this.plugin = Main.getInstance();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		//economy send [oldacc] [new acc] [money]
		if (command.getName().equalsIgnoreCase("economy")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage("§cNur Spieler dürfen /economy nutzen");
				return true;
			}
			Player p = (Player) sender;

			if (args.length == 1) {
				if (args[0].equals("create")) {
					if(!p.hasPermission("Economy.command.create")){ p.sendMessage(plugin.getNoPermission()); return true;}
					plugin.getPlayerAccount().get(p).createNewAccount();
					p.sendMessage(plugin.getPrefix() + "§aDu hast nun ein neues Konto");
				} else if (args[0].equals("money")) {
					if(!p.hasPermission("Economy.command.money")){ p.sendMessage(plugin.getNoPermission()); return true;}
					plugin.getPlayerAccount().get(p).displayAllAccount();
				}
			} else if (args.length == 2) {
				if (args[0].equals("delete")) {
					if(!p.hasPermission("Economy.command.delete")){ p.sendMessage(plugin.getNoPermission()); return true;}
					plugin.getPlayerAccount().get(p).deleteAccount(args[1]);
					p.sendMessage(plugin.getPrefix() + "§aDu hast das Konto §e" + args[1] + " §aGelöscht");
				}else if (args[0].equals("money")) {
					if(!p.hasPermission("Economy.command.money")){ p.sendMessage(plugin.getNoPermission()); return true;}
					plugin.getPlayerAccount().get(p).displayMoney(args[1]);
				}else if(args[0].equals("transaktionen")){
					if(!p.hasPermission("Economy.command.transaktionen")){ p.sendMessage(plugin.getNoPermission()); return true;}
					plugin.getPlayerAccount().get(p).displayLastTransaktionen(args[1]);
				}
			} else if (args.length == 5) {
				if (args[0].equals("send")) {
					if(!p.hasPermission("Economy.command.send")){ p.sendMessage(plugin.getNoPermission()); return true;}
					plugin.getPlayerAccount().get(p).sendMoneyToOtherAccont(p, args[1], args[2], args[3], Integer.parseInt(args[4]));
					p.sendMessage(plugin.getPrefix() + "§aÜberweisung Erfolgrich!");
				}
			}

		}
		return true;

	}

}
