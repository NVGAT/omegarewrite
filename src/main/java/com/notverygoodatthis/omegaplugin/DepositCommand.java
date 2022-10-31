package com.notverygoodatthis.omegaplugin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            try {
                Player p = (Player) sender;
                OmegaPlayer player = new OmegaPlayer(p);
                int lifeAmount = Integer.parseInt(args[0]);
                int playerLives = player.getOmegaLives();
                if(lifeAmount > playerLives) {
                    sender.sendMessage("§b§lCan't withdraw more lives than you have");
                } else if(lifeAmount == playerLives) {
                    player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), OmegaPlugin.getLife(lifeAmount));
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getPlayerInstance().getName(), "You've deposited all of your lives. Thank you for playing on the Omega SMP.", null, "Omega SMP plugin");
                    player.getPlayerInstance().kickPlayer("You've deposited all of your lives. Thank you for playing on the Omega SMP.");
                } else {
                    Bukkit.getLogger().info(String.valueOf(playerLives - lifeAmount));
                    player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), OmegaPlugin.getLife(lifeAmount));
                    player.setOmegaLives(playerLives - lifeAmount);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("§b§lInvalid life format");
            }
            return true;
        }
        return false;
    }
}
