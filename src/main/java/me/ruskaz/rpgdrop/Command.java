package me.ruskaz.rpgdrop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Command implements CommandExecutor {

    private final Plugin plugin = RPGDrop.getPlugin(RPGDrop.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command. Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("illegalArgumentsMessage"))));
        }
        else if (args[0].equalsIgnoreCase("clear")) {
            for (World world : Bukkit.getWorlds()) {
                for (Item item : world.getEntitiesByClass(Item.class)) {
                    if (!ItemOperations.isItemInProtectionList(item.getItemStack())) continue;
                    ItemOperations.clearItem(item.getItemStack());
                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("clearProtectionMessage"))));
        }
        else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            RPGDrop.config = plugin.getConfig();
            RPGDrop.configManager = new ConfigurationManager();
            RPGDrop.configManager.checkForDependencies();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("configReloadMessage"))));
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("illegalArgumentsMessage"))));
        }
        return true;
    }
}