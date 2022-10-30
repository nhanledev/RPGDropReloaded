package me.ruskaz.rpgdrop;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        if (this.getConfig().getBoolean("mythicMobsSupport")) {
            Bukkit.getPluginManager().registerEvents(new EventsMythic(), this);
            Bukkit.getLogger().log(Level.INFO,"MythicMobs support turned on.");
        }
        if (this.getConfig().getLong("timeToProtect") != 0) new TimeManager();
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
                    ItemStack item = ((Item) entity).getItemStack();
                    if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                        ItemMeta meta = item.getItemMeta();
                        List<Component> lore = meta.lore();
                        if (lore.contains(Component.text("affected"))) {
                            if (lore.size() == 3) {
                                lore = null;
                            } else {
                                lore.remove(lore.size() - 1);
                                lore.remove(lore.size() - 1);
                                lore.remove(lore.size() - 1);
                            }
                            meta.lore(lore);
                            item.setItemMeta(meta);
                        }
                    }
                }
            }
        }
    }
}
