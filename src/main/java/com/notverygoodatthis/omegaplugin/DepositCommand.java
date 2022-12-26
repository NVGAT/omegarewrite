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
            //Try/catch since there could be a NumberFormatException if the user doesn't specify how many life items they want or specifies it in a string
            try {
                //We cast the player into an OmegaPlayer object
                Player p = (Player) sender;
                OmegaPlayer player = new OmegaPlayer(p);
                //We store the amount of lives they want to deposit and the amount of lives they have in integer variables
                int lifeAmount = Integer.parseInt(args[0]);
                int playerLives = player.getOmegaLives();
                //If they don't have enough lives we don't let them deposit
                if(lifeAmount > playerLives) {
                    sender.sendMessage("§b§lCan't withdraw more lives than you have");
                } else if(lifeAmount == playerLives) {
                    //If they have the exact amount of lives they've deposited we ban them from the server and drop the life items to the ground
                    player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), OmegaPlugin.getLife(lifeAmount));
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getPlayerInstance().getName(), "You've deposited all of your lives. Thank you for playing on the Omega SMP.", null, "Omega SMP plugin");
                    player.getPlayerInstance().kickPlayer("You've deposited all of your lives. Thank you for playing on the Omega SMP.");
                } else {
                    //And if none of these are true we just drop the life items to the ground and let them deposit.
                    Bukkit.getLogger().info(String.valueOf(playerLives - lifeAmount));
                    player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), OmegaPlugin.getLife(lifeAmount));
                    player.setOmegaLives(playerLives - lifeAmount);
                }
            } catch (NumberFormatException e) {
                //If a NumberFormatException occurred we know that they haven't provided the command with a valid integer, so we notify them about that
                sender.sendMessage("§b§lInvalid life format");
            }
            return true;
        }
        return false;
    }
}
