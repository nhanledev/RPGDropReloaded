package me.ruskaz.rpgdrop.dependencytools;

import com.alessiodp.parties.api.Parties;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartiesTools {

    public static boolean isPlayerInPartyWith(Player p1, UUID p2) {
        return Parties.getApi().areInTheSameParty(p1.getUniqueId(), p2);
    }

}
