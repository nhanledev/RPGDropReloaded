package me.ruskaz.rpgdrop;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static io.papermc.paper.text.PaperComponents.plainSerializer;

public class Command implements CommandExecutor {

    private final Plugin plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command. Command command, String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("illegalArgumentsMessage"))));
            return true;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Item entity : world.getEntitiesByClass(Item.class)) {
                    ItemStack item = entity.getItemStack();
                    if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                        ItemMeta meta = item.getItemMeta();
                        List<Component> lore = meta.lore();
                        for (int i = 0; i < lore.size(); i++) {
                            String l = plainSerializer().serialize(lore.get(i));
                            if (!l.contains("affected")) continue;
                            lore = ItemOperations.clearLore(lore);
                            meta.lore(lore);
                            item.setItemMeta(meta);
                            break;
                        }
                    }
                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("clearProtectionMessage"))));
        }
        else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("configReloadMessage"))));
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("illegalArgumentsMessage"))));
        }
        return true;
    }
}