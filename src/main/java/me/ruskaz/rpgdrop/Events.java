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
        Player killer = e.getEntity().getKiller();
        if (killer == null || !killer.hasPermission("rpgdrop.protection")) return;

        List<ItemStack> dropped = e.getDrops();
        for (ItemStack item : dropped) {
            addProtection(item, killer);
            startProtectionTimer(item);
        }
    }

    @EventHandler
    public void preventPickingUp(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        if (!isItemProtected(item)) return;

        UUID ownerUUID = getProtectionOwner(item);
        if (ownerUUID == null) return;

        if (e.getEntity() instanceof Player player) {
            if (canPlayerPickupItem(player, ownerUUID)) {
                clearProtection(item);
            } else {
                e.setCancelled(true); // message handled internally in canPlayerPickupItem
            }
        } else {
            if (!RPGDrop.configManager.getMobCanPickUp()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void preventItemsInHopper(InventoryPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        if (!isItemProtected(item)) return;

        if (RPGDrop.configManager.getPreventFromHoppers()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void clearTagsOnLeave(PlayerQuitEvent e) {
        if (!RPGDrop.configManager.getClearProtectionOnLeave()) return;

        for (World world : Bukkit.getWorlds()) {
            for (Item item : world.getEntitiesByClass(Item.class)) {
                ItemStack droppedItem = item.getItemStack();
                if (!isItemProtected(droppedItem)) continue;

                clearProtectionForPlayer(droppedItem, e.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void clearTagsOnDeath(PlayerDeathEvent e) {
        if (!RPGDrop.configManager.getClearProtectionOnDeath()) return;

        for (World world : Bukkit.getWorlds()) {
            for (Item item : world.getEntitiesByClass(Item.class)) {
                ItemStack droppedItem = item.getItemStack();
                if (!isItemProtected(droppedItem)) continue;

                clearProtectionForPlayer(droppedItem, e.getEntity());
            }
        }
    }
}