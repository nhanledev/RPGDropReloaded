package me.ruskaz.rpgdrop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemOperations {

    public static boolean isItemInProtectionList(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
        return findProtectionLineIndex(item) != -1;
    }

    public static void beginProtection(ItemStack item) {
        long timeToProtect = Math.round(RPGDrop.config.getDouble("timeToProtect"));
        if (timeToProtect <= 0) return;
        Bukkit.getScheduler().runTaskLater(RPGDrop.plugin, () -> clearItem(item),  timeToProtect * 20L);
    }
    public static void modifyItem(ItemStack item, String player) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add("RPGDrop:" + player);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    public static void modifyItem(ItemStack item, Player player) {
        modifyItem(item, player.getUniqueId());
    }
    public static void modifyItem(ItemStack item, UUID player) {
        modifyItem(item, player.toString());
    }

    public static void clearItem(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.size() == 1) {
            lore = null;
        }
        else lore.removeIf(line -> line.contains("RPGDrop"));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void clearItem(ItemStack item, Player player) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.size() == 1) {
            lore = null;
        }
        else lore.removeIf(line -> line.contains(player.getUniqueId().toString()));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static int findProtectionLineIndex(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return -1;
        int index = -1;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for (int i = 0; lore.size() > i; i++) {
            if (lore.get(i).contains("RPGDrop")) {
                index = i;
                break;
            }
        }
        return index;
    }
}
