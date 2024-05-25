package me.ruskaz.rpgdrop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Command implements CommandExecutor {

    private final Plugin plugin = RPGDrop.getPlugin(RPGDrop.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command. Command command, String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("illegalArgumentsMessage"))));
            return true;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            RPGDrop.droppedItems.clear();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("clearProtectionMessage"))));
        }
        else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            RPGDrop.config = plugin.getConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("configReloadMessage"))));
        }
        else if (args[0].equalsIgnoreCase("kys")) {
            for (ItemStack item : RPGDrop.droppedItems.keySet()) {
                sender.sendMessage(item.getType().name());
            }
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("illegalArgumentsMessage"))));
        }
        return true;
    }
}