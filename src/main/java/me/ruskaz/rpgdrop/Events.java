package me.ruskaz.rpgdrop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Events implements Listener {

    private final Plugin plugin = RPGDrop.getPlugin(RPGDrop.class);

    @EventHandler(ignoreCancelled = true)
    public void addTags(EntityDeathEvent e) {
        try {
            if (e.getEntity().getKiller() == null) return;
            Player killer = e.getEntity().getKiller();
            if (killer.hasPermission("rpgdrop.protection")) {
                List<ItemStack> dropped = e.getDrops();
                for (ItemStack item : dropped) {
                    item = item.asOne();
                    RPGDrop.droppedItems.put(item, killer.getUniqueId() + ":" + System.currentTimeMillis());
                    ItemOperations.beginProtection(item);
                }
            }
        } catch (NullPointerException ignored) {}
    }

    @EventHandler
    public void preventPickingUp(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack().asOne();
        if (!ItemOperations.isItemInProtectionList(item)) return;
        String getter = RPGDrop.droppedItems.get(item);
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.hasPermission("rpgdrop.bypass") || getter.split(":")[0].equals(player.getUniqueId().toString())) {
                RPGDrop.droppedItems.remove(item);
            } else {
                e.setCancelled(true);
            }
        } else {
            if (!plugin.getConfig().getBoolean("mobsCanPickUp")) return;
            if (ItemOperations.isItemInProtectionList(item)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventInHopperlikeBlocks(InventoryPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack().asOne();
        if (ItemOperations.isItemInProtectionList(item) && plugin.getConfig().getBoolean("preventFromHoppers")) e.setCancelled(true);
    }

    @EventHandler
    public void clearTagsOnLeave(PlayerQuitEvent e) {
        if (!plugin.getConfig().getBoolean("clearProtectionOnLeave")) return;
        Set<Map.Entry<ItemStack, String>> protectedItems = RPGDrop.droppedItems.entrySet();
        for (Map.Entry<ItemStack, String> protectionKeys : protectedItems) {
            ItemStack protectedItem = protectionKeys.getKey();
            String protectionPlayerUUID = protectionKeys.getValue().split(":")[0];

            if (protectionPlayerUUID.equals(e.getPlayer().getUniqueId().toString())) RPGDrop.droppedItems.remove(protectedItem);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void clearTagsOnDeath(PlayerDeathEvent e) {
        if (!plugin.getConfig().getBoolean("clearProtectionOnDeath")) return;
        Set<Map.Entry<ItemStack, String>> protectedItems = RPGDrop.droppedItems.entrySet();
        for (Map.Entry<ItemStack, String> protectionKeys : protectedItems) {
            ItemStack protectedItem = protectionKeys.getKey();
            String protectionPlayerUUID = protectionKeys.getValue().split(":")[0];

            if (protectionPlayerUUID.equals(e.getEntity().getUniqueId().toString())) RPGDrop.droppedItems.remove(protectedItem);
        }
    }
}
