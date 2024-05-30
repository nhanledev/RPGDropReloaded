package me.ruskaz.rpgdrop.dependencytools;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.ruskaz.rpgdrop.ItemOperations;
import me.ruskaz.rpgdrop.RPGDrop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EventsMythic implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void addTagsMythic(MythicMobDeathEvent e) {
        if (e.getKiller() == null) return;
        if (e.getKiller() instanceof Player) {
            Player killer = (Player) e.getKiller();
            if (killer.hasPermission("rpgdrop.protection")) {
                List<ItemStack> dropped = e.getDrops();
                for (ItemStack item : dropped) {
                    ItemOperations.modifyItem(item, killer);
                    ItemOperations.beginProtection(item);
                }
            }
        }
    }
}
