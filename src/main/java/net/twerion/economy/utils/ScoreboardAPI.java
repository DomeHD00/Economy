package net.twerion.economy.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.twerion.economy.Main;

public class ScoreboardAPI {

	public void sendScoreboard(Player p){
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective score = board.registerNewObjective("aaa", "bbb");
		
		score.setDisplayName("§6EconomySystem");
		score.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		score.getScore("§5Konten").setScore(Main.getInstance().getPlayerAccount().get(p).getAccounts().size());
		score.getScore("§5Coins").setScore((int)Main.getInstance().getPlayerAccount().get(p).getAllMoney());
		
		p.setScoreboard(score.getScoreboard());
	}
	
	public void updateScoreboard(Player p){
		Objective score = p.getScoreboard().getObjective("aaa");
		
		score.getScore("§5Konten").setScore(Main.getInstance().getPlayerAccount().get(p).getAccounts().size());
		score.getScore("§5Coins").setScore((int)Main.getInstance().getPlayerAccount().get(p).getAllMoney());
		
		
	}
	
}
