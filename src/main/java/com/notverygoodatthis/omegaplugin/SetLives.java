package com.notverygoodatthis.omegaplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLives implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("omegasmp.admin")) {
            Player target = Bukkit.getPlayerExact(args[0]);
            int newLives = Integer.parseInt(args[1]);
            try {
                if(newLives < 5) {
                    OmegaPlugin.playerLives.remove(target.getName(), OmegaPlugin.playerLives.get(target.getName()));
                    OmegaPlugin.playerLives.put(target.getName(), newLives);
                    for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.playerListName(Component.text("[" + OmegaPlugin.playerLives.get(target.getName()) + "] " + target.getName()));
                    }
                } else {
                    sender.sendMessage(Component.text("Life count over five not allowed"));
                }
            } catch(NullPointerException e) {
                sender.sendMessage("Invalid target");
            } catch(NumberFormatException e) {
                sender.sendMessage("Invalid value set for new lives");
            }
        }
        return false;
    }
}
