package me.ruskaz.rpgdrop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class RPGDrop extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginCommand("rpgdrop").setExecutor(new Command());
        if (this.getConfig().getBoolean("mythicMobsSupport")) {
            Bukkit.getPluginManager().registerEvents(new EventsMythic(), this);
            Bukkit.getLogger().log(Level.INFO, "[RPGDrop] >>> MythicMobs support turned on.");
        }
        if (this.getConfig().getLong("timeToProtect") != 0) new TimeManager();
    }
}
