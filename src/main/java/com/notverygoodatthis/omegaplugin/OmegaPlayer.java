package com.notverygoodatthis.omegaplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OmegaPlayer {
    //LifeOutcome enum, used to check if setting a player's lives went through successfully
    public enum LifeOutcome {
        SUCCESS,
        FAILURE
    }

    //Actual player object
    private Player player;

    //Constructor, this is where we link up the player objects
    public OmegaPlayer(Player player) {
        this.player = player;
    }

    //Getter for the player instance, used for tampering with the Player object from the Bukkit API
    public Player getPlayerInstance() {
        return player;
    }

    //Getter for the amount of lives the player has
    public int getOmegaLives() {
        return OmegaPlugin.playerLives.get(player.getName());
    }

    //Method to set the amount of lives a player has
    public LifeOutcome setOmegaLives(int newValue) {
        //If the new value is less or equal to five...
        if(newValue <= 5) {
            //We remove the player from the playerLives hashmap in the OmegaPlugin class
            OmegaPlugin.playerLives.remove(player.getName(), OmegaPlugin.playerLives.get(player.getName()));
            //Then we put them back in with the new life amount
            OmegaPlugin.playerLives.put(player.getName(), newValue);
            //We log that setting the lives went successfully to the server console
            Bukkit.getLogger().info("Setting " + player.getName() + "'s lives went through with status SUCCESS");
            //And we return SUCCESS from the LifeOutcome enumerator
            return LifeOutcome.SUCCESS;
        } else {
            //If the new value is not less or equal to five, we log the failure and return FAILURE from the LifeOutcome enumerator
            Bukkit.getLogger().info("Tried to set " + player.getName() + "'s life count to " + newValue + " but it was greater than five. Process went through with status FAILURE.");
            return LifeOutcome.FAILURE;
        }
    }

    public void updateTablist() {
        //Updates the current player's tablist
        player.setPlayerListName("[" + OmegaPlugin.playerLives.get(player.getName()) + "] " + player.getName());
    }
}