package com.notverygoodatthis.omegaplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DepositCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            try {
                Player target = (Player) sender;
                int lifeAmount = Integer.parseInt(args[0]);
                int playerLives = OmegaPlugin.playerLives.get(target.getName());
                if(lifeAmount > playerLives) {
                    sender.sendMessage("Can't withdraw more lives than you have");
                } else if(lifeAmount == playerLives) {
                    target.getWorld().dropItemNaturally(target.getLocation(), OmegaPlugin.getLife(lifeAmount));
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), "You've deposited all of your lives. Thank you for playing on the Omega SMP.", null, "Omega SMP plugin");
                    target.kick(Component.text("You've deposited all of your lives. Thank you for playing on the Omega SMP."));
                } else {
                    Bukkit.getLogger().info(String.valueOf(playerLives - lifeAmount));
                    target.getWorld().dropItemNaturally(target.getLocation(), OmegaPlugin.getLife(lifeAmount));
                    OmegaPlugin.playerLives.remove(target.getName(), OmegaPlugin.playerLives.get(target.getName()));
                    OmegaPlugin.playerLives.put(target.getName(), playerLives - lifeAmount);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid life format");
            }
            return true;
        }
        return false;
    }
}
