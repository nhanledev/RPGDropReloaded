package me.ruskaz.rpgdrop;

import me.ruskaz.rpgdrop.dependencytools.MMOCoreTools;
import me.ruskaz.rpgdrop.dependencytools.PartiesTools;
import me.ruskaz.rpgdrop.dependencytools.SimpleClansTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ItemOperations {

    private static final String PROTECTION_PREFIX = "RPGDrop:";
    private static final Map<UUID, Long> lastWarningTime = new ConcurrentHashMap<>();

    public static boolean isItemProtected(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;

        return meta.getLore().stream().anyMatch(line -> line.startsWith(PROTECTION_PREFIX));
    }

    public static void startProtectionTimer(ItemStack item) {
//        long protectionSeconds = RPGDrop.configManager.getTimeToProtect();
//        if (protectionSeconds <= 0 || itemEntity == null || !itemEntity.isValid()) return;
//
//        Bukkit.getScheduler().runTaskLater(RPGDrop.plugin, () -> {
//            if (!itemEntity.isValid()) return;
//            clearProtection(itemEntity.getItemStack());
//        }, protectionSeconds * 20L);
    }

    public static void addProtection(ItemStack item, UUID ownerUUID) {
        if (item == null || ownerUUID == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.add(PROTECTION_PREFIX + ownerUUID);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void addProtection(ItemStack item, Player player) {
        if (player != null) {
            addProtection(item, player.getUniqueId());
        }
    }

    public static void clearProtection(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;

        List<String> lore = new ArrayList<>(meta.getLore());
        lore.removeIf(line -> line.startsWith(PROTECTION_PREFIX));
        meta.setLore(lore.isEmpty() ? null : lore);
        item.setItemMeta(meta);
    }

    public static void clearProtectionForPlayer(ItemStack item, Player player) {
        if (item == null || player == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;

        UUID uuid = player.getUniqueId();
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.removeIf(line -> line.equals(PROTECTION_PREFIX + uuid));
        meta.setLore(lore.isEmpty() ? null : lore);
        item.setItemMeta(meta);
    }

    public static UUID getProtectionOwner(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return null;

        for (String line : meta.getLore()) {
            if (line.startsWith(PROTECTION_PREFIX)) {
                try {
                    return UUID.fromString(line.substring(PROTECTION_PREFIX.length()));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return null;
    }

    public static boolean canPlayerPickupItem(Player player, UUID ownerUUID) {
        if (player == null || ownerUUID == null) return true;

        if (player.hasPermission("rpgdrop.bypass") || player.getUniqueId().equals(ownerUUID)) {
            return true;
        }

        if (RPGDrop.configManager.getMMOCoreSupport() &&
                MMOCoreTools.playersAreTogether(player, ownerUUID)) {
            return true;
        }

        if (RPGDrop.configManager.getSimpleClansSupport() &&
                SimpleClansTools.isPlayerInClanWith(player, ownerUUID)) {
            return true;
        }

        if (RPGDrop.configManager.getPartiesSupport() &&
                PartiesTools.isPlayerInPartyWith(player, ownerUUID)) {
            return true;
        }

        // Blocked — send throttled message
        sendThrottledPickupWarning(player);
        return false;
    }

    private static void sendThrottledPickupWarning(Player player) {
        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();
        long last = lastWarningTime.getOrDefault(uuid, 0L);

        if (now - last >= 1000) {
            lastWarningTime.put(uuid, now);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(RPGDrop.plugin.getConfig().getString("canNotPickUp"))));
        }
    }
}