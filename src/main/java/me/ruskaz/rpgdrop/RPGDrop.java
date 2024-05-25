package me.ruskaz.rpgdrop;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
public final class RPGDrop extends JavaPlugin {

    public static Map<ItemStack, String> droppedItems;
    public static Plugin plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginCommand("rpgdrop").setExecutor(new Command());
        if (this.getConfig().getBoolean("mythicMobsSupport")) {
            if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
                Bukkit.getPluginManager().registerEvents(new EventsMythic(), this);
                Bukkit.getLogger().log(Level.INFO, "[RPGDrop] MythicMobs support enabled.");
            }
            else {
                Bukkit.getLogger().log(Level.WARNING, "[RPGDrop] MythicMobs wasn't detected, support not enabled.");
            }
        }
        droppedItems = new HashMap<>();
        plugin = this;
        config = plugin.getConfig();

        // Clears items of old protection method
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Item entity : world.getEntitiesByClass(Item.class)) {
                ItemStack item = entity.getItemStack();
                if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                    ItemMeta meta = item.getItemMeta();
                    List<Component> lore = meta.lore();
                    for (int i = 0; i < lore.size(); i++) {
                        String l = PaperComponents.plainSerializer().serialize(lore.get(i));
                        if (!l.contains("affected")) continue;
                        for (int j = 0; i < lore.size(); i++) {
                            String line = PaperComponents.plainSerializer().serialize(lore.get(j));
                            if (line.contains("affected")) {
                                lore.remove(lore.get(j));
                            }
                        }
                        meta.lore(lore);
                        item.setItemMeta(meta);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        RPGDrop.droppedItems.clear();
    }
}
