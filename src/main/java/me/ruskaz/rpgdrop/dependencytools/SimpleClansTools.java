package me.ruskaz.rpgdrop.dependencytools;

import me.ruskaz.rpgdrop.RPGDrop;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SimpleClansTools {

    public static boolean isPlayerInClanWith(Player p1, UUID p2) {
        ClanPlayer clanPlayer = SimpleClans.getInstance().getClanManager().getClanPlayer(p1);
        if (clanPlayer != null && clanPlayer.getClan() != null) {
            if (clanPlayer.getClan().isMember(p2)) return true;
            ClanPlayer clanPlayer2 = SimpleClans.getInstance().getClanManager().getClanPlayer(p2);
            if (clanPlayer2 != null && clanPlayer2.getClan() != null) {
                Clan secondPClan = clanPlayer2.getClan();
                return RPGDrop.configManager.getSimpleClansAlliedClansShareLoot() && secondPClan.getAllies().contains(clanPlayer.getClan().getName());
            }
        }
        return false;
    }

}
