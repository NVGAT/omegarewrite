package com.notverygoodatthis.omegaplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCheck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.getWorld() == OmegaPlugin.spawnLocation.getWorld()) {
                if(player.getLocation().distance(OmegaPlugin.spawnLocation) < 50) {
                    player.sendMessage("<Omega SMP> You're in range of spawn. If you kill here you will automatically be banned.");
                } else {
                    player.sendMessage("<Omega SMP> You're not in range of spawn. You can kill without any consequences.");
                }
            } else {
                player.sendMessage("<OmegaPlugin> You're not in the overworld. You cannot be punished for killing here.");
            }
        }
        return false;
    }
}
