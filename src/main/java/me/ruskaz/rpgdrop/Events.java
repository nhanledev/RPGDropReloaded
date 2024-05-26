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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Events implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void addTags(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player killer = e.getEntity().getKiller();
        if (killer.hasPermission("rpgdrop.protection")) {
            List<ItemStack> dropped = e.getDrops();
            for (ItemStack item : dropped) {
                item = item.asOne();
                RPGDrop.droppedItems.put(item, killer.getUniqueId());
                ItemOperations.beginProtection(item);
            }
        }
    }

    @EventHandler
    public void preventPickingUp(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack().asOne();
        if (!ItemOperations.isItemInProtectionList(item)) return;
        UUID playerUUID = RPGDrop.droppedItems.get(item);
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            boolean canPickUp = player.hasPermission("rpgdrop.bypass");
            canPickUp = canPickUp || playerUUID.equals(player.getUniqueId());
            if (RPGDrop.mmoCoreSupport) {
                canPickUp = canPickUp || MMOCoreTools.playersAreTogether(player, playerUUID);
            }
            if (RPGDrop.simpleClansSupport) {
                canPickUp = canPickUp || SimpleClansTools.isPlayerInClanWith(player, playerUUID);
            }
            if (RPGDrop.partiesSupport) {
                canPickUp = canPickUp || PartiesTools.isPlayerInPartyWith(player, playerUUID);
            }
            if (canPickUp) RPGDrop.droppedItems.remove(item);
            else e.setCancelled(true);
        } else {
            if (!RPGDrop.config.getBoolean("mobsCanPickUp")) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventItemsInHopper(InventoryPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack().asOne();
        if (!ItemOperations.isItemInProtectionList(item)) return;
        if (RPGDrop.config.getBoolean("preventFromHoppers")) e.setCancelled(true);
    }

    @EventHandler
    public void clearTagsOnLeave(PlayerQuitEvent e) {
        if (!RPGDrop.config.getBoolean("clearProtectionOnLeave")) return;
        Set<Map.Entry<ItemStack, UUID>> protectedItems = RPGDrop.droppedItems.entrySet();
        for (Map.Entry<ItemStack, UUID> protectionKeys : protectedItems) {
            ItemStack protectedItem = protectionKeys.getKey();
            UUID protectionPlayerUUID = protectionKeys.getValue();
            if (protectionPlayerUUID.equals(e.getPlayer().getUniqueId())) RPGDrop.droppedItems.remove(protectedItem);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void clearTagsOnDeath(PlayerDeathEvent e) {
        if (!RPGDrop.config.getBoolean("clearProtectionOnDeath")) return;
        Set<Map.Entry<ItemStack, UUID>> protectedItems = RPGDrop.droppedItems.entrySet();
        for (Map.Entry<ItemStack, UUID> protectionKeys : protectedItems) {
            ItemStack protectedItem = protectionKeys.getKey();
            UUID protectionPlayerUUID = protectionKeys.getValue();
            if (protectionPlayerUUID.equals(e.getEntity().getUniqueId())) RPGDrop.droppedItems.remove(protectedItem);
        }
    }

}
