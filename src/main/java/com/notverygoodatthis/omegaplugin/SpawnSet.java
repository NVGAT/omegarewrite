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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            List<Double> cordsList = Arrays.asList(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
            Bukkit.getPluginManager().getPlugin("OmegaPlugin").getConfig().set("spawn-cords", cordsList);
            sender.sendMessage(MessageFormat.format("<Omega SMP> Successful! Your spawn coordinates are now {0} {1} {2}", cordsList.get(0), cordsList.get(1), cordsList.get(2)));
        } catch(NumberFormatException e) {
            sender.sendMessage("<Omega SMP> Invalid number format");
        }
        return false;
    }
}
