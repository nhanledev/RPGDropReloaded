package me.ruskaz.rpgdrop;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPGDrop extends JavaPlugin {

    public static Plugin plugin;
    public static FileConfiguration config;

    public static ConfigurationManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginCommand("rpgdrop").setExecutor(new Command());
        plugin = this;
        config = plugin.getConfig();

        configManager = new ConfigurationManager();

        configManager.checkForDependencies();
    }

    @Override
    public void onDisable() {
        for (World world : Bukkit.getWorlds()) {
            for (Item item : world.getEntitiesByClass(Item.class)) {
                if (!ItemOperations.isItemInProtectionList(item.getItemStack())) continue;
                ItemOperations.clearItem(item.getItemStack());
            }
        }
    }
}
