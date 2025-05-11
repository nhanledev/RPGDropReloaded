package me.ruskaz.rpgdrop;

import me.ruskaz.rpgdrop.dependencytools.MMOCoreTools;
import me.ruskaz.rpgdrop.dependencytools.PartiesTools;
import me.ruskaz.rpgdrop.dependencytools.SimpleClansTools;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemOperations {

    private static final NamespacedKey PROTECTION_KEY = new NamespacedKey(RPGDrop.plugin, "rpgdrop_owner");
    private static final Map<UUID, Long> lastWarningTime = new ConcurrentHashMap<>();

    public static boolean isItemProtected(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(PROTECTION_KEY, PersistentDataType.STRING);
    }

    public static void startProtectionTimer(ItemStack item) {
        // You can implement this again using an item entity if needed.
    }

    public static void addProtection(ItemStack item, UUID ownerUUID) {
        if (item == null || ownerUUID == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.getPersistentDataContainer().set(PROTECTION_KEY, PersistentDataType.STRING, ownerUUID.toString());
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
        if (meta == null) return;

        meta.getPersistentDataContainer().remove(PROTECTION_KEY);
        item.setItemMeta(meta);
    }

    public static void clearProtectionForPlayer(ItemStack item, Player player) {
        if (item == null || player == null) return;
        UUID uuid = player.getUniqueId();
        UUID currentOwner = getProtectionOwner(item);
        if (uuid.equals(currentOwner)) {
            clearProtection(item);
        }
    }

    public static UUID getProtectionOwner(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        String ownerString = meta.getPersistentDataContainer().get(PROTECTION_KEY, PersistentDataType.STRING);
        if (ownerString == null) return null;

        try {
            return UUID.fromString(ownerString);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
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