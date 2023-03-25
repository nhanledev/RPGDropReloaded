package me.ruskaz.rpgdrop;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static io.papermc.paper.text.PaperComponents.plainSerializer;

public class ItemOperations {

    public static List<Component> clearLore (List<Component> lore) {
        if (lore.size() == 1) return null;
        for (int i = 0; i < lore.size(); i++) {
            String line = plainSerializer().serialize(lore.get(i));
            if (line.contains("affected")) {
                lore.remove(lore.get(i));
            }
        }
        return lore;
    }

    public static List<Component> clearLore (List<Component> lore, Player p) {
        if (lore.size() == 1) return null;
        for (int i = 0; lore.size() > i; i++) {
            String line = plainSerializer().serialize(lore.get(i));
            if (line.contains("affected") && line.contains(String.valueOf(p.getUniqueId()))) {
                lore.remove(lore.get(i));
            }
        }
        return lore;
    }

    public static void addLore (ItemStack item, Player killer) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore;
        if (meta.lore() == null) lore = new ArrayList<>();
        else lore = meta.lore();
        lore.add(Component.text("affected:" + killer.getUniqueId() + ":" + System.currentTimeMillis()));
        meta.lore(lore);
        item.setItemMeta(meta);
    }
}
