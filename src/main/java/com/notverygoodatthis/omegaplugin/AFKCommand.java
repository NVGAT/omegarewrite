package com.notverygoodatthis.omegaplugin;

import me.NoChance.PvPManager.PvPManager;
import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(Bukkit.getPluginManager().isPluginEnabled("PvPManager")) {
                PvPManager pvpManager = (PvPManager) Bukkit.getPluginManager().getPlugin("PVPManager");
                PvPlayer player = pvpManager.getPlayerHandler().get((Player) sender);
                player.setPvP(!player.hasPvPEnabled());
                player.getPlayer().sendMessage("<Omega SMP> You are now protected from PvP. Type ");
            }
        }
        return false;
    }
}
