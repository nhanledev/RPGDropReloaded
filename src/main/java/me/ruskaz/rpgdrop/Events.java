package me.ruskaz.rpgdrop;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Item;
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

import java.util.List;
import java.util.UUID;

import static me.ruskaz.rpgdrop.ItemOperations.*;

public class Events implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void addTags(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player killer = e.getEntity().getKiller();
        if (killer.hasPermission("rpgdrop.protection")) {
            List<ItemStack> dropped = e.getDrops();
            for (ItemStack item : dropped) {
                modifyItem(item, killer);
                beginProtection(item);
            }
        }
    }

    @EventHandler
    public void preventPickingUp(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        if (!isItemInProtectionList(item)) return;
        UUID playerUUID = UUID.fromString(item.getItemMeta().getLore().get(findProtectionLineIndex(item)).split(":")[1]);
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (canPlayerPickUpItem(player, playerUUID)) clearItem(item);
            else e.setCancelled(true);
        } else {
            if (!RPGDrop.configManager.getMobCanPickUp()) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventItemsInHopper(InventoryPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        if (!isItemInProtectionList(item)) return;
        if (RPGDrop.configManager.getPreventFromHoppers()) e.setCancelled(true);
    }

    @EventHandler
    public void clearTagsOnLeave(PlayerQuitEvent e) {
        if (!RPGDrop.configManager.getClearProtectionOnLeave()) return;
        for (World world : Bukkit.getWorlds()) {
            for (Item item : world.getEntitiesByClass(Item.class)) {
                ItemStack droppedItem = item.getItemStack();
                if (!ItemOperations.isItemInProtectionList(droppedItem)) continue;
                clearItem(droppedItem, e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void clearTagsOnDeath(PlayerDeathEvent e) {
        if (!RPGDrop.configManager.getClearProtectionOnDeath()) return;
        for (World world : Bukkit.getWorlds()) {
            for (Item item : world.getEntitiesByClass(Item.class)) {
                ItemStack droppedItem = item.getItemStack();
                if (!ItemOperations.isItemInProtectionList(droppedItem)) continue;
                clearItem(droppedItem, e.getEntity());
            }
        }
    }
}
