package me.ruskaz.rpgdrop.dependencytools;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.ruskaz.rpgdrop.ItemOperations;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EventsMythic implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void addTagsMythic(MythicMobDeathEvent e) {
        Player killer = (Player) e.getKiller();
        if (killer == null || !killer.hasPermission("rpgdrop.protection")) return;

        List<ItemStack> dropped = e.getDrops();
        for (ItemStack item : dropped) {
            ItemOperations.addProtection(item, killer);
            ItemOperations.startProtectionTimer(item);
        }
    }
}
