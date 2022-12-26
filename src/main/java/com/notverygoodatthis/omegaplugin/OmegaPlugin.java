package com.notverygoodatthis.omegaplugin;

import dev.dbassett.skullcreator.SkullCreator;
import me.NoChance.PvPManager.PvPManager;
import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public final class OmegaPlugin extends JavaPlugin implements Listener {
    //Public variables defined for use everywhere
    public static HashMap<String, Integer> playerLives = new HashMap<>();
    public static List<String> rewardMats = new ArrayList<>();
    public static List<Player> omegaGappledPlayers = new ArrayList<>();
    public static final String LIFE_ITEM_NAME = "§a§lLife";
    public static final String REVIVAL_ITEM_NAME = "§4§oRevive item";
    public static final String RESURRECTION_SHARD_NAME = "§b§lRessurection fragment";
    public static Location spawnLocation;
    PvPManager pvpManager;

    @Override
    public void onEnable() {
        //On enabling the plugin we do most of the basic stuff
        registerCommands();
        registerRecipes();
        //Such as reading from the configs and loading all of the players and their lives into their respected lists
        FileConfiguration config = getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        List<String> playerList = (List<String>) config.getList("players");
        List<Integer> lifeList = (List<Integer>) config.getList("lives");
        rewardMats = (List<String>) config.getList("reward-mats");

        //Then we neatly structure the players and their lives in a hashmap, making every player's statistics easily accessible
        if(playerList != null) {
            for(String p: playerList) {
                playerLives.put(p, lifeList.get(playerList.indexOf(p)));
                if(!Bukkit.getOfflinePlayer(p).isBanned()) {
                    getLogger().info(p + " has " + playerLives.get(p) + " lives.");
                } else {
                    getLogger().info(p + " has lost all of their lives and are banned");
                }
            }
        }

        List<Double> spawnCords = (List<Double>) getConfig().getList("spawn-cords");
        getServer().getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                spawnLocation = new Location(Bukkit.getWorld("world"), spawnCords.get(0), spawnCords.get(1), spawnCords.get(2));
            }
        }, 20L);

        getLogger().info("The plugin will check if PVPManager is installed in thirty seconds. PVPManager isn't necessary, but it's the best option for use with this plugin.");

        getServer().getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if(Bukkit.getPluginManager().isPluginEnabled("PVPManager")) {
                    pvpManager = (PvPManager) Bukkit.getPluginManager().getPlugin("PVPManager");
                    getLogger().info("PVPManager has been detected. The plugin will attempt to work with PVPManager to get players out of combat when they get banned.");
                } else {
                    getLogger().info("PVPManager has not been detected. If you don't want to use PVP-log plugins you can ignore this message.");
                }
            }
        }, 20L * 30);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        //If the server config does not contain the name of a player that joined (they've never played before) they are greeted with an explanation of the SMP
        if(!getCurrentPlayerlist().contains(e.getPlayer().getName())) {
            playerLives.put(e.getPlayer().getName(), 5);
            e.getPlayer().sendMessage("§b§l<Omega SMP> Welcome to the " +
                    "§b§lOmega SMP! You have five lives, each of which you lose when dying to a player. Lose them all and you get banned. " +
                    "§b§lHave fun!");
            saveLives();
        }
        //We then update the tab list to ensure correct display of each player's lives
        updateTablist();
    }

    //A function to save all of the lives into the config
    void saveLives() {
        getConfig().set("players", getCurrentPlayerlist());
        getConfig().set("lives", getCurrentLifeList());
        saveConfig();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        //The core logic of the plugin, implementing the core feature of it: the five lives system
        if(e.getEntity().getKiller() != null) {
            //First we wrap the player object in our custom OmegaPlayer wrapper
            OmegaPlayer player = new OmegaPlayer(e.getEntity());
            //If the player hasn't lost all of their lives we just simply decrease their life count
            if(player.getOmegaLives() - 1 != 0) {
                player.setOmegaLives(player.getOmegaLives() - 1);
            } else {
                //On the other hand, if they did we pick a random item material from the list we loaded from the config
                Random rand = new Random();
                ItemStack randItem = new ItemStack(Material.matchMaterial(rewardMats.get(rand.nextInt(rewardMats.size()))));
                //We apply each item's respected properties, such as names, enchantments etc.
                switch (randItem.getType()) {
                    case NETHERITE_CHESTPLATE:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_LEGGINGS:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.SWIFT_SNEAK, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_SWORD:
                        randItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
                        randItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
                        randItem.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 4);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_AXE:
                        randItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
                        randItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    case NETHERITE_HELMET:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        randItem.addUnsafeEnchantment(Enchantment.OXYGEN, 5);
                        randItem.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                        break;
                    case NETHERITE_BOOTS:
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        randItem.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        randItem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 6);
                        randItem.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 5);
                        break;
                    case ENCHANTED_GOLDEN_APPLE:
                        ItemMeta meta = randItem.getItemMeta();;
                        meta.setDisplayName("§b§l[ O M E G A   A P P L E ]");
                        randItem.setItemMeta(meta);
                        break;
                }
                //Then we log the item dropped at the ban event of the player
                getLogger().info("Dropped " + randItem.getType().toString() + " at the death of " + player.getPlayerInstance().getName());
                //After that we actually drop the item
                player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), randItem);
                //Generate the ban message
                String banMsg = "You have lost your last life to " + player.getPlayerInstance().getKiller().getName() + ". Thank you for playing on the Omega SMP.";
                //Drop a resurrection shard at the player's death location
                player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), getResurrectionShard(1));
                if(Bukkit.getPluginManager().getPlugin("PVPManager") != null) {
                    PvPlayer pvPlayer = pvpManager.getPlayerHandler().get(player.getPlayerInstance());
                    pvPlayer.setPvP(false);
                    getLogger().info("Got " + pvPlayer.getName() + " out of combat for the purpose of banning them. This is intentional behavior.");
                }
                //We ban the player, ensuring they won't connect again
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getPlayerInstance().getName(), banMsg, null, "Omega SMP plugin");
                player.getPlayerInstance().kickPlayer(banMsg);
            }
        }

        //And finally we update the tab list
        updateTablist();

        //Logic for the automatic spawn-killer-ban system, first we check if we should be banning them at all by checking the punish-on-spawn-kill boolean that's stored in the config
        boolean banEnabled = getConfig().getBoolean("punish-on-spawn-kill");
        if(e.getEntity().getKiller() instanceof Player && banEnabled) {
            if(spawnLocation.distance(e.getEntity().getKiller().getLocation()) < 50) {
                //Then we use the PVPManager API to get the killer in a PvPlayer object
                PvPlayer killer = pvpManager.getPlayerHandler().get(e.getEntity().getKiller());
                //We get the killer out of PVP so that they don't drop all of their stuff on the ground
                killer.setPvP(false);
                //Then we ban the killer, simple as that!
                String banMSG = "You've been banned due to killing " + e.getEntity().getName() + " at spawn. Contact the server admin to discuss the length of your punishment, otherwise this ban will last forever.";
                Bukkit.getBanList(BanList.Type.NAME).addBan(killer.getName(), banMSG, null, "Omega SMP plugin");
            }
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        //This is the logic for the omega apples
        if(e.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals("§b§l[ O M E G A   A P P L E ]")) {
            //If the player consumed an omega apple, and they're not in the list...
            if(!omegaGappledPlayers.contains(e.getPlayer())) {
                //We initialize a new OmegaPlayer object with the player in the constructor
                OmegaPlayer player = new OmegaPlayer(e.getPlayer());
                //We add all the omega potion effects and add them to the omega apple list
                player.getPlayerInstance().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60, 5));
                player.getPlayerInstance().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60, 3));
                omegaGappledPlayers.add(player.getPlayerInstance());
                //Then we schedule a task to run in five minutes, this acts as a cooldown
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        omegaGappledPlayers.remove(player.getPlayerInstance());
                    }
                }, 20L * 300);
                player.getPlayerInstance().sendMessage("<Omega SMP> You've been buffed from eating an Omega apple, you're now on cooldown for five more minutes.");
            } else {
                //If they are already on the omega apple list we just send them a message and cancel the event
                e.getPlayer().sendMessage("<Omega SMP> Your Omega apple cooldown is still active. default god apple effects applied");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        //Logic for the buffed creeper drop rates, you can remove this if you want to
        if(e.getDrops().contains(Material.GUNPOWDER)) {
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.GUNPOWDER, 5));
        }
    }

    @EventHandler
    public void onCommandExecute(ServerCommandEvent e) {
        if(e.getCommand().contains("omega") || e.getCommand().contains("deposit")) {
            //If a command contains the keywords "omega" or "deposit" we update the tablist and save the life values. This is only for the console.
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
        //Same thing as the ServerCommandEvent, except this time for players.
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
        //If the item in the player's inventory contains an ItemMeta...
        if(e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
            //And they've right-clicked something...
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //And if the item is a life item...
                if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(LIFE_ITEM_NAME) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                    //Then we initialize an OmegaPlayer object with the subject player in the constructor
                    OmegaPlayer player = new OmegaPlayer(e.getPlayer());
                    //We try to increment their lives, and if it's successful...
                    if(player.setOmegaLives(player.getOmegaLives() + 1) == OmegaPlayer.LifeOutcome.SUCCESS) {
                        //We take one life item away and update the tablist
                        player.getPlayerInstance().getInventory().getItemInMainHand().setAmount(player.getPlayerInstance().getInventory().getItemInMainHand().getAmount() - 1);
                        player.updateTablist();
                    } else {
                        //If the outcome is not successful, we don't take away anything and the life count stays the same. This indicates that the player
                        //is already at the maximum amount of lives. We send them a message and update the tablist.
                        player.getPlayerInstance().sendMessage("<Omega SMP> You've reached the maximum life count of five lives already");
                        player.updateTablist();
                    }
                }
            }
        }
    }

    void registerCommands() {
        //Registering all the commands in the plugin
        getCommand("omegaset").setExecutor(new SetLives());
        getCommand("deposit").setExecutor(new DepositCommand());
        getCommand("omegarevive").setExecutor(new ReviveCommand());
        getCommand("omegaitem").setExecutor(new OPItemCommand());
        getCommand("spawncheck").setExecutor(new SpawnCheck());
        getCommand("spawnset").setExecutor(new SpawnSet());
    }

    void registerRecipes() {
        //Registering the recipes
        Bukkit.addRecipe(revivalRecipe());
    }
    void updateTablist() {
        //Tablist update logic
        for(Player p : getServer().getOnlinePlayers()) {
            //We make an OmegaPlayer object for every player in the server, adding them in the constructor
            OmegaPlayer player = new OmegaPlayer(p);
            //Then we set their name in the tablist accordingly
            player.getPlayerInstance().setPlayerListName("[" + player.getOmegaLives() + "] " + p.getName());
        }
    }

    public static ItemStack getLife(int amount) {
        //Getter for the life item, important for efficiency
        ItemStack life = new ItemStack(Material.POPPED_CHORUS_FRUIT, amount);
        ItemMeta meta = life.getItemMeta();
        meta.setDisplayName(LIFE_ITEM_NAME);
        life.setItemMeta(meta);
        return life;
    }

    public static ItemStack getRevivalHead(int amount) {
        //Getter for the revival item
        ItemStack skull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ2MDdhZThhNmY5Mzc0MmU4ZWIxNmEwZjg2MjY1OWUzMDg3NjEwMTlhMzk3NzIyYzFhZmU4NGIxNzlkMWZhMiJ9fX0=");
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(REVIVAL_ITEM_NAME);
        skull.setItemMeta(meta);
        skull.setAmount(amount);
        return skull;
    }

    public static ItemStack getResurrectionShard(int amount) {
        //Getter for the ressurection shard
        ItemStack shard = new ItemStack(Material.COCOA_BEANS, amount);
        ItemMeta meta = shard.getItemMeta();
        meta.setDisplayName(RESURRECTION_SHARD_NAME);
        shard.setItemMeta(meta);
        return shard;
    }

    List<Integer> getCurrentLifeList() {
        //Gets the current life list from the config
        return new ArrayList<>(playerLives.values());
    }

    List<String> getCurrentPlayerlist() {
        //Gets the current player list from the config
        return new ArrayList<>(playerLives.keySet());
    }

    public ShapedRecipe revivalRecipe() {
        //Revival item recipe
        ItemStack revivalItem = getRevivalHead(1);
        NamespacedKey key = new NamespacedKey(this, "player_head");
        ShapedRecipe rec = new ShapedRecipe(key, revivalItem);
        rec.shape("TDT", "DSD", "TDT");
        rec.setIngredient('D', Material.DIAMOND_BLOCK);
        rec.setIngredient('T', Material.TOTEM_OF_UNDYING);
        rec.setIngredient('S', new RecipeChoice.ExactChoice(getResurrectionShard(1)));
        return rec;
    }

    @Override
    public void onDisable() {
        //On disabling the plugin we save the config with the life values inside.
        saveLives();
        saveConfig();
    }
}