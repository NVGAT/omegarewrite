package com.notverygoodatthis.omegaplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OmegaPlayer {
    public enum LifeOutcome {
        SUCCESS,
        FAILURE
    }

    private Player player;

    public OmegaPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayerInstance() {
        return player;
    }

    public int getOmegaLives() {
        return OmegaPlugin.playerLives.get(player.getName());
    }

    public LifeOutcome setOmegaLives(int newValue) {
        if(newValue <= 5) {
            OmegaPlugin.playerLives.remove(player.getName(), OmegaPlugin.playerLives.get(player.getName()));
            OmegaPlugin.playerLives.put(player.getName(), newValue);
            Bukkit.getLogger().info("Setting " + player.getName() + "'s lives went through with status SUCCESS");
            return LifeOutcome.SUCCESS;
        } else {
            Bukkit.getLogger().info("Tried to set " + player.getName() + "'s life count to " + newValue + " but it was greater than five. Process went through with status FAILURE.");
            return LifeOutcome.FAILURE;
        }
    }

    public void updateTablist() {
        player.setPlayerListName("[" + OmegaPlugin.playerLives.get(player.getName()) + "] " + player.getName());
    }
}