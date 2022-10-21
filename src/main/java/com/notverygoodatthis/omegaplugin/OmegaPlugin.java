package com.notverygoodatthis.omegaplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class OmegaPlugin extends JavaPlugin implements Listener {
    public static HashMap<String, Integer> playerLives = new HashMap<>();
    public static final String LIFE_ITEM_NAME = "§a§lLife";

    @Override
    public void onEnable() {
        registerCommands();
        FileConfiguration config = getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        List<String> playerList = (List<String>) config.getList("players");
        List<Integer> lifeList = (List<Integer>) config.getList("lives");

        if(playerList != null) {
            for(String p: playerList) {
                playerLives.put(p, lifeList.get(playerList.indexOf(p)));
                printPlayerLives();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        getLogger().info(e.getPlayer().getName() + " joined lmao");
        if(!getCurrentPlayerlist().contains(e.getPlayer().getName())) {
            playerLives.put(e.getPlayer().getName(), 5);
            e.getPlayer().sendMessage(Component.text(NamedTextColor.AQUA + "" + TextDecoration.BOLD + "<Omega SMP> Welcome to the " +
                    "Omega SMP! You have five lives, each of which you lose when dying to a player. Lose them all and you get banned. " +
                    "Have fun!"));
            saveLives();
        }
        updateTablist();
    }

    void saveLives() {
        getConfig().set("players", getCurrentPlayerlist());
        getConfig().set("lives", getCurrentLifeList());
        saveConfig();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getPlayer().getKiller() != null) {
            Player player = e.getPlayer();
            if(getPlayerLifeCount(player.getName()) - 1 != 0) {
                setPlayerLifeCount(player.getName(), getPlayerLifeCount(player.getName()) - 1);
            } else {
                String banMsg = "You have lost your last life to " + player.getKiller().getName() + ". Thank you for playing on the Omega SMP.";
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), banMsg, null, "Omega SMP plugin");
                player.kick(Component.text(banMsg));
            }
        }
        updateTablist();
    }

    @EventHandler
    public void onCommandExecute(ServerCommandEvent e) {
        if(e.getCommand().contains("omega") || e.getCommand().contains("deposit")) {
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    updateTablist();
                    saveLives();
                }
            }, 20L);
        }
        }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().contains("omega") || e.getMessage().contains("deposit")) {
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    updateTablist();
                    saveLives();
                }
            }, 20L);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
            TextComponent itemInHandName = (TextComponent) e.getPlayer().getInventory().getItemInMainHand().getItemMeta().displayName();
            if(e.getAction().isRightClick() && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                assert itemInHandName != null;
                if(itemInHandName.content().equals(LIFE_ITEM_NAME)) {
                    e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                    setPlayerLifeCount(e.getPlayer().getName(), getPlayerLifeCount(e.getPlayer().getName()) + 1);
                    updateTablist();
                    saveLives();
                }
            }
        }
    }

    void registerCommands() {
        getCommand("omegaset").setExecutor(new SetLives());
        getCommand("deposit").setExecutor(new DepositCommand());
    }
    void updateTablist() {
        for(Player p : getServer().getOnlinePlayers()) {
            p.playerListName(Component.text("[" + getPlayerLifeCount(p.getName()) + "] " + p.getName()));
        }
    }

    public void setPlayerLifeCount(String p, int newCount) {
        playerLives.remove(p, playerLives.get(p));
        playerLives.put(p, newCount);
        saveLives();
    }

    public static ItemStack getLife(int amount) {
        ItemStack life = new ItemStack(Material.POPPED_CHORUS_FRUIT, amount);
        ItemMeta meta = life.getItemMeta();
        meta.displayName(Component.text(LIFE_ITEM_NAME));
        life.setItemMeta(meta);
        return life;
    }

    public int getPlayerLifeCount(String p) {
        return playerLives.get(p);
    }

    List<Integer> getCurrentLifeList() {
        return new ArrayList<>(playerLives.values());
    }

    List<String> getCurrentPlayerlist() {
        return new ArrayList<>(playerLives.keySet());
    }

    void printPlayerLives() {
        for(String p : playerLives.keySet()) {
            getLogger().info(p + " has " + playerLives.get(p) + " lives.");
        }
    }
}
