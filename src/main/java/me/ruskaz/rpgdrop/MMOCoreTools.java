package me.ruskaz.rpgdrop;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.guild.provided.Guild;
import net.Indyuce.mmocore.party.provided.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MMOCoreTools implements Listener {
    public static boolean isPlayerInPartyWith(Player p1, UUID p2) {
        Party party = (Party) MMOCore.plugin.partyModule.getParty(PlayerData.get(p1));
        if (party != null) return party.hasMember(p2) && RPGDrop.config.getBoolean("mmoCoreSupport.partySupport");
        return false;
    }

    public static boolean isPlayerInGuildWith(Player p1, UUID p2) {
        Guild guild = (Guild) MMOCore.plugin.guildModule.getGuild(PlayerData.get(p1));
        if (guild != null) return guild.hasMember(p2) && RPGDrop.config.getBoolean("mmoCoreSupport.guildSupport");
        return false;
    }

    public static boolean isPlayerFriendsWith(Player p1, UUID p2) {
        return PlayerData.get(p1).getFriends().contains(p2) && RPGDrop.config.getBoolean("mmoCoreSupport.friendsSupport");
    }

    public static boolean playersAreTogether(Player p1, UUID p2) {
        return isPlayerInGuildWith(p1, p2) || isPlayerInPartyWith(p1, p2) || isPlayerFriendsWith(p1, p2);
    }
}
