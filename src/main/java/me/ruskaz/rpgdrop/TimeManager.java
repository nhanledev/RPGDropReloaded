package me.ruskaz.rpgdrop;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

import static io.papermc.paper.text.PaperComponents.plainSerializer;

public class TimeManager {

    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    private final Plugin plugin = Main.getPlugin(Main.class);

    public TimeManager() {
        long period = plugin.getConfig().getLong("timeToProtect");
        scheduler.runTaskTimer(plugin, () -> {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Chunk chunk : world.getLoadedChunks()) {
                    for (Entity entity : chunk.getEntities()) {
                        if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
                            ItemStack item = ((Item) entity).getItemStack();
                            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                                ItemMeta meta = item.getItemMeta();
                                List<Component> lore = meta.lore();
                                for (int i = 0; i < lore.size(); i++) {
                                    String l = plainSerializer().serialize(lore.get(i));
                                    if (!l.contains("affected")) continue;
                                    String[] splitted = l.split(":");
                                    String time = splitted[2];
                                    long recharge = Long.parseLong(time);
                                    if ((System.currentTimeMillis() - recharge) / 1000L >= period) {
                                        lore = Events.clearLore(lore);
                                        meta.lore(lore);
                                        item.setItemMeta(meta);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 0L, 20L);
    }
}
