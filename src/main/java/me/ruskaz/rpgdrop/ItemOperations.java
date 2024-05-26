package me.ruskaz.rpgdrop;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class ItemOperations {

    public static boolean isItemInProtectionList(ItemStack item) {
        return RPGDrop.droppedItems.containsKey(item);
    }

    public static void beginProtection(ItemStack item) {
        long timeToProtect = Math.round(RPGDrop.config.getDouble("timeToProtect"));
        if (timeToProtect <= 0) return;
        Bukkit.getScheduler().runTaskLater(RPGDrop.plugin, () -> RPGDrop.droppedItems.remove(item),  timeToProtect * 20L);
    }
}
