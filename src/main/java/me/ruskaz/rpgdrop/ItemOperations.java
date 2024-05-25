package me.ruskaz.rpgdrop;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemOperations {

    public static boolean isItemInProtectionList(ItemStack item) {
        if (RPGDrop.droppedItems.containsKey(item)) {
            return true;
        }
        return RPGDrop.droppedItems.containsKey(item);
    }
    public static void beginProtection(ItemStack item) {
        long timeToProtect = Math.round(RPGDrop.config.getDouble("timeToProtect"));
        if (timeToProtect <= 0) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                RPGDrop.droppedItems.remove(item);
            }
        }.runTaskLater(RPGDrop.plugin, timeToProtect * 20L);
    }
}
