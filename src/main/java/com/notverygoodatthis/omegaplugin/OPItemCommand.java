package com.notverygoodatthis.omegaplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OPItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Simple logic for the /omegaitem command, used by ONLY operators for debugging purposes
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("omegasmp.admin")) {
                ItemStack item = new ItemStack(Material.AIR);
                switch(args[0]) {
                    //Netherite sword
                    case "sword":
                        item.setType(Material.NETHERITE_SWORD);
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
                        item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
                        item.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 4);
                        item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    //Netherite axe
                    case "axe":
                        item.setType(Material.NETHERITE_AXE);
                        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                        item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
                        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
                        item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    //Netherite helmet
                    case "helmet":
                        item.setType(Material.NETHERITE_HELMET);
                        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        item.addUnsafeEnchantment(Enchantment.OXYGEN, 5);
                        item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                        break;
                    //Netherite chestplate
                    case "chestplate":
                        item.setType(Material.NETHERITE_CHESTPLATE);
                        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    //Netherite leggings
                    case "leggings":
                        item.setType(Material.NETHERITE_LEGGINGS);
                        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        item.addUnsafeEnchantment(Enchantment.SWIFT_SNEAK, 5);
                        item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        break;
                    //Netherite boots
                    case "boots":
                        item.setType(Material.NETHERITE_BOOTS);
                        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 6);
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                        item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                        item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 6);
                        item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 5);
                        break;
                    //Omega apple
                    case "apple":
                        item.setType(Material.ENCHANTED_GOLDEN_APPLE);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("§b§l[ O M E G A   A P P L E ]");
                        item.setItemMeta(meta);
                        break;
                    //Resurrection shard
                    case "shard":
                        item = OmegaPlugin.getResurrectionShard(1);
                        break;
                }
                //Drops the item at the command sender's location
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                return true;
            }
        }
        return false;
    }
}
