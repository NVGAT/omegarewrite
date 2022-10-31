package com.notverygoodatthis.omegaplugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.getInventory().getItemInMainHand() == OmegaPlugin.getRevivalHead(player.getInventory().getItemInMainHand().getAmount())) {
                Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "§b§lYou have successfully revived " + args[0] + "§b§l. Contact the server owner if there was any errors");
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                return true;
            }
        }
        return false;
    }
}
