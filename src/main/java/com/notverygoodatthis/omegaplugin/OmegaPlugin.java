package com.notverygoodatthis.omegaplugin;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.w3c.dom.Text;

import java.util.*;
import java.util.List;

public final class OmegaPlugin extends JavaPlugin implements Listener {
    public static HashMap<String, Integer> playerLives = new HashMap<>();
    public static List<String> rewardMats = new ArrayList<>();
    public static List<Player> omegaGappledPlayers = new ArrayList<>();
    public static final String LIFE_ITEM_NAME = "§a§lLife";
    public static final String REVIVAL_ITEM_NAME = "§4§oRevive item";
    public static final String RESURRECTION_SHARD_NAME = "§b§lRessurection fragment";

    @Override
    public void onEnable() {
        registerCommands();
        registerRecipes();
        FileConfiguration config = getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        List<String> playerList = (List<String>) config.getList("players");
        List<Integer> lifeList = (List<Integer>) config.getList("lives");
        rewardMats = (List<String>) config.getList("reward-mats");

        if(playerList != null) {
            for(String p: playerList) {
                playerLives.put(p, lifeList.get(playerList.indexOf(p)));
                printPlayerLives();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!getCurrentPlayerlist().contains(e.getPlayer().getName())) {
            playerLives.put(e.getPlayer().getName(), 5);
            e.getPlayer().sendMessage("§b§l<Omega SMP> Welcome to the " +
                    "§b§lOmega SMP! You have five lives, each of which you lose when dying to a player. Lose them all and you get banned. " +
                    "§b§lHave fun!");
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
        if(e.getEntity().getKiller() != null) {
            OmegaPlayer player = new OmegaPlayer(e.getEntity());
            if(player.getOmegaLives() - 1 != 0) {
                player.setOmegaLives(player.getOmegaLives() - 1);
            } else {
                Random rand = new Random();
                ItemStack randItem = new ItemStack(Material.matchMaterial(rewardMats.get(rand.nextInt(rewardMats.size()))));
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
                getLogger().info("Dropped " + randItem.getType().toString() + " at the death of " + player.getPlayerInstance().getName());
                player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), randItem);
                String banMsg = "You have lost your last life to " + player.getPlayerInstance().getKiller().getName() + ". Thank you for playing on the Omega SMP.";
                player.getPlayerInstance().getWorld().dropItemNaturally(player.getPlayerInstance().getLocation(), getResurrectionShard(1));
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getPlayerInstance().getName(), banMsg, null, "Omega SMP plugin");
                player.getPlayerInstance().kickPlayer(banMsg);
            }
        }
        updateTablist();
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        if(e.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals("§b§l[ O M E G A   A P P L E ]")) {
            OmegaPlayer player = new OmegaPlayer(e.getPlayer());
            player.getPlayerInstance().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60, 5));
            player.getPlayerInstance().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60, 3));
        }
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
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(LIFE_ITEM_NAME) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                    OmegaPlayer player = new OmegaPlayer(e.getPlayer());
                    player.setOmegaLives(player.getOmegaLives() + 1);
                    player.updateTablist();
                    if(player.setOmegaLives(player.getOmegaLives() + 1) == OmegaPlayer.LifeOutcome.SUCCESS) {
                        player.getPlayerInstance().getInventory().getItemInMainHand().setAmount(player.getPlayerInstance().getInventory().getItemInMainHand().getAmount() - 1);
                    } else {

                    }
                }
            }
        }
    }

    void registerCommands() {
        getCommand("omegaset").setExecutor(new SetLives());
        getCommand("deposit").setExecutor(new DepositCommand());
        getCommand("omegarevive").setExecutor(new ReviveCommand());
        getCommand("omegaitem").setExecutor(new OPItemCommand());
    }

    void registerRecipes() {
        Bukkit.addRecipe(revivalRecipe());
    }
    void updateTablist() {
        for(Player p : getServer().getOnlinePlayers()) {
            OmegaPlayer player = new OmegaPlayer(p);
            player.getPlayerInstance().setPlayerListName("[" + player.getOmegaLives() + "] " + p.getName());
        }
    }

    public static ItemStack getLife(int amount) {
        ItemStack life = new ItemStack(Material.POPPED_CHORUS_FRUIT, amount);
        ItemMeta meta = life.getItemMeta();
        meta.setDisplayName(LIFE_ITEM_NAME);
        life.setItemMeta(meta);
        return life;
    }

    public static ItemStack getRevivalHead(int amount) {
        ItemStack skull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ2MDdhZThhNmY5Mzc0MmU4ZWIxNmEwZjg2MjY1OWUzMDg3NjEwMTlhMzk3NzIyYzFhZmU4NGIxNzlkMWZhMiJ9fX0=");
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(REVIVAL_ITEM_NAME);
        skull.setItemMeta(meta);
        skull.setAmount(amount);
        return skull;
    }

    public static ItemStack getResurrectionShard(int amount) {
        ItemStack shard = new ItemStack(Material.COCOA_BEANS, amount);
        ItemMeta meta = shard.getItemMeta();
        meta.setDisplayName(RESURRECTION_SHARD_NAME);
        shard.setItemMeta(meta);
        return shard;
    }

    List<Integer> getCurrentLifeList() {
        return new ArrayList<>(playerLives.values());
    }

    List<String> getCurrentPlayerlist() {
        return new ArrayList<>(playerLives.keySet());
    }

    void printPlayerLives() {
        for (String p : playerLives.keySet()) {
            getLogger().info(p + " has " + playerLives.get(p) + " lives.");
        }
    }

    public ShapedRecipe revivalRecipe() {
        ItemStack revivalItem = getRevivalHead(1);
        NamespacedKey key = new NamespacedKey(this, "player_head");
        ShapedRecipe rec = new ShapedRecipe(key, revivalItem);
        rec.shape("TDT", "DSD", "TDT");
        rec.setIngredient('D', Material.DIAMOND_BLOCK);
        rec.setIngredient('T', Material.TOTEM_OF_UNDYING);
        rec.setIngredient('S', new RecipeChoice.ExactChoice(getResurrectionShard(1)));
        return rec;
    }
}