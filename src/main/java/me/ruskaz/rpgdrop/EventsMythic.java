package me.ruskaz.rpgdrop;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EventsMythic implements Listener {

    @EventHandler
    public void addTagsMythic(MythicMobDeathEvent e) {
        try {
            if (e.getKiller() == null) return;
            if (e.getKiller() instanceof Player) {
                Player killer = (Player) e.getKiller();
                if (killer.hasPermission("rpgdrop.protection")) {
                    List<ItemStack> dropped = e.getDrops();
                    for (ItemStack item : dropped) {
                        item = item.asOne();
                        RPGDrop.droppedItems.put(item, killer.getUniqueId() + ":" + System.currentTimeMillis());
                        ItemOperations.beginProtection(item);
                    }
                }
            }
        } catch (NullPointerException ignored) {}
    }
}
