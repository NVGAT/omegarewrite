package com.notverygoodatthis.omegaplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class SpawnSet implements CommandExecutor {
    @Override
    //SpawnSet command, used by operators to define where spawn is.
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            //We store the coordinates in a list of Doubles
            List<Double> cordsList = Arrays.asList(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
            //We set the spawn-cords list to the newly defined cordsList.
            Bukkit.getPluginManager().getPlugin("OmegaPlugin").getConfig().set("spawn-cords", cordsList);
            Bukkit.getPluginManager().getPlugin("OmegaPlugin").saveConfig();
            //We notify the command sender that they've successfully set the spawn location.
            sender.sendMessage(MessageFormat.format("<Omega SMP> Successful! Your spawn coordinates are now {0} {1} {2}", cordsList.get(0), cordsList.get(1), cordsList.get(2)));
        } catch(NumberFormatException e) {
            //If they've defined the numbers wrong we notify them about it.
            sender.sendMessage("<Omega SMP> Invalid number format");
        }
        return false;
    }
}
