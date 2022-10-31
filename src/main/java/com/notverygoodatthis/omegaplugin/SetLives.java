package com.notverygoodatthis.omegaplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLives implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("omegasmp.admin")) {
            OmegaPlayer target = new OmegaPlayer(Bukkit.getPlayerExact(args[0]));
            int newLives = Integer.parseInt(args[1]);
            try {
                if(newLives <= 5) {
                    target.setOmegaLives(newLives);
                    target.updateTablist();
                } else {
                    sender.sendMessage("§b§lLife count over five not allowed");
                }
            } catch(NullPointerException e) {
                sender.sendMessage("§b§lInvalid target");
            } catch(NumberFormatException e) {
                sender.sendMessage("§b§lInvalid value set for new lives");
            }
            return true;
        }
        return false;
    }
}
