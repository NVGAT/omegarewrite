package com.notverygoodatthis.omegaplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLives implements CommandExecutor {
    @Override
    //Simple command to set a player's lives. Used by operators.
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //If the sender is an admin
        if(sender.hasPermission("omegasmp.admin")) {
            //We store the target in an OmegaPlayer object
            OmegaPlayer target = new OmegaPlayer(Bukkit.getPlayerExact(args[0]));
            //We store the new amount of lives in an integer
            int newLives = Integer.parseInt(args[1]);
            try {
                //If the new amount of lives is greater or equal to five...
                if(newLives <= 5) {
                    //We set the target's lives and update the tablist
                    target.setOmegaLives(newLives);
                    target.updateTablist();
                } else {
                    //And if it's not we notify the command sender about it
                    sender.sendMessage("§b§lLife count over five not allowed");
                }
            //If a NullPointerException occurs that means that they've mistyped a target name
            } catch(NullPointerException e) {
                sender.sendMessage("§b§lInvalid target");
            }
            //And if a NumberFormatException occurs that means that they've selected an invalid new lives number
            catch(NumberFormatException e) {
                sender.sendMessage("§b§lInvalid value set for new lives");
            }
            return true;
        }
        return false;
    }
}
