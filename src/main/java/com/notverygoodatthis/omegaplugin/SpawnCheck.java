package com.notverygoodatthis.omegaplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCheck implements CommandExecutor {
    @Override
    //Command to check whether a player is in spawn. Used by normal players to figure out where they can and can't kill.
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            //If the sender is a player we wrap them in a Player object
            Player player = (Player) sender;
            //If they're in the overworld...
            if(player.getWorld() == OmegaPlugin.spawnLocation.getWorld()) {
                //And if they're less than fifty blocks away from the spawn location (pre-defined in the OmegaPlugin class)...
                if(player.getLocation().distance(OmegaPlugin.spawnLocation) < 50) {
                    //Then we tell the player that they are in spawn and that if they kill here they will be banned.
                    player.sendMessage("<Omega SMP> You're in range of spawn. If you kill here you will automatically be banned.");
                    return true;
                } else {
                    //If the player isn't within fifty blocks of spawn, we notify them that they can kill here without any consequences.
                    player.sendMessage("<Omega SMP> You're not in range of spawn. You can kill without any consequences.");
                    return true;
                }
            } else {
                //If the player isn't in the overworld we notify them that they can kill anywhere in this dimension.
                player.sendMessage("<OmegaPlugin> You're not in the overworld. You cannot be punished for killing here.");
            }
        }
        return false;
    }
}
