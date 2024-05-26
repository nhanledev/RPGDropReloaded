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
import java.util.UUID;
import java.util.logging.Level;

public final class RPGDrop extends JavaPlugin {

    public static Map<ItemStack, UUID> droppedItems;
    public static Plugin plugin;
    public static FileConfiguration config;
    public static boolean mythicMobsSupport = false;
    public static boolean mmoCoreSupport = false;
    public static boolean simpleClansSupport = false;
    public static boolean partiesSupport = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginCommand("rpgdrop").setExecutor(new Command());
        droppedItems = new HashMap<>();
        plugin = this;
        config = plugin.getConfig();

        checkForDependencies();

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

    public static void checkForDependencies() {
        if (plugin.getConfig().getBoolean("mythicMobsSupport")) {
            if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
                if (!RPGDrop.mythicMobsSupport) Bukkit.getPluginManager().registerEvents(new EventsMythic(), RPGDrop.plugin);
                Bukkit.getLogger().log(Level.INFO, "[RPGDrop] MythicMobs support enabled.");
                mythicMobsSupport = true;
            }
            else {
                Bukkit.getLogger().log(Level.WARNING, "[RPGDrop] MythicMobs wasn't detected, support not enabled.");
            }
        }
        if (plugin.getConfig().getBoolean("mmoCoreSupport.enabled")) {
            if (Bukkit.getPluginManager().getPlugin("MMOCore") != null) {
                Bukkit.getLogger().log(Level.INFO, "[RPGDrop] MMOCore support enabled.");
                mmoCoreSupport = true;
            }
            else {
                Bukkit.getLogger().log(Level.WARNING, "[RPGDrop] MMOCore wasn't detected, support not enabled.");
            }
        }
        if (plugin.getConfig().getBoolean("simpleClansSupport.enabled")) {
            if (Bukkit.getPluginManager().getPlugin("SimpleClans") != null) {
                Bukkit.getLogger().log(Level.INFO, "[RPGDrop] SimpleClans support enabled.");
                simpleClansSupport = true;
            }
            else {
                Bukkit.getLogger().log(Level.WARNING, "[RPGDrop] SimpleClans wasn't detected, support not enabled.");
            }
        }
        if (plugin.getConfig().getBoolean("partiesSupport")) {
            if (Bukkit.getPluginManager().getPlugin("Parties") != null) {
                Bukkit.getLogger().log(Level.INFO, "[RPGDrop] Parties support enabled.");
                partiesSupport = true;
            }
            else {
                Bukkit.getLogger().log(Level.WARNING, "[RPGDrop] Parties wasn't detected, support not enabled.");
            }
        }
    }

    @Override
    public void onDisable() {
        RPGDrop.droppedItems.clear();
    }
}
