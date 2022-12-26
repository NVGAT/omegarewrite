package com.notverygoodatthis.omegaplugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Command to revive a player using the revival item
        if(sender instanceof Player) {
            //If the sender is a player we store them in a Player object
            Player player = (Player) sender;
            //If they're holding a player head (the only way to get a player head is to craft a revival item)...
            if(player.getInventory().getItemInMainHand().getType() == OmegaPlugin.getRevivalHead(1).getType()) {
                //We store the targeted player in an OfflinePlayer object
                OfflinePlayer bannedPlayer = Bukkit.getOfflinePlayer(args[0]);
                //If the player is banned...
                if(bannedPlayer.isBanned()) {
                    //Then we unban them and notify the command sender that they've just revived a player.
                    Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "§b§lYou have successfully revived " + args[0] + "§b§l. Contact the server owner if there was any errors");
                    //We (obviously) take away one revival item because they've just used it
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                    //Then we store the targeted player in an OmegaPlayer
                    OmegaPlayer omegaPlayer = new OmegaPlayer(bannedPlayer.getPlayer());
                    //And we set their life count to five.
                    omegaPlayer.setOmegaLives(5);
                    return true;
                } else {
                    //If the targeted player isn't banned we notify the command sender about it.
                    player.sendMessage("§b§l" + args[0] + " isn't banned!");
                }
            }
        }
        return false;
    }
}
