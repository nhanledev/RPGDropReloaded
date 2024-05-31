package me.ruskaz.rpgdrop;

import me.ruskaz.rpgdrop.dependencytools.EventsMythic;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class ConfigurationManager {
    private final boolean mythicMobsSupport;
    private boolean mythicEventsRegistered = false;

    private final boolean mmoCoreSupport;
    private final boolean mmocorePartiesSupport;
    private final boolean mmocoreGuildsSupport;
    private final boolean mmocoreFriendsSupport;

    private final boolean simpleClansSupport;
    private final boolean simpleClansAlliedClansShareLoot;

    private final boolean partiesSupport;

    private final boolean mobCanPickUp;
    private final boolean preventFromHoppers;
    private final boolean clearProtectionOnLeave;
    private final boolean clearProtectionOnDeath;

    private final long timeToProtect;

    ConfigurationManager() {
        FileConfiguration config = RPGDrop.config;
        this.mythicMobsSupport = config.getBoolean("mythicMobsSupport") && Bukkit.getPluginManager().getPlugin("MythicMobs") != null;

        this.mmoCoreSupport = config.getBoolean("mmocoreSupport.enabled") && Bukkit.getPluginManager().getPlugin("MMOCore") != null;
        this.mmocorePartiesSupport = mmoCoreSupport && config.getBoolean("mmocoreSupport.partySupport");
        this.mmocoreGuildsSupport = mmoCoreSupport && config.getBoolean("mmocoreSupport.guildSupport");
        this.mmocoreFriendsSupport = mmoCoreSupport && config.getBoolean("mmocoreSupport.friendsSupport");

        this.simpleClansSupport = config.getBoolean("simpleClansSupport.enabled") && Bukkit.getPluginManager().getPlugin("SimpleClans") != null;
        this.simpleClansAlliedClansShareLoot = simpleClansSupport && config.getBoolean("simpleClansSupport.alliedClansShareLoot");

        this.partiesSupport = config.getBoolean("partiesSupport") && Bukkit.getPluginManager().getPlugin("Parties") != null;

        this.mobCanPickUp = config.getBoolean("mobCanPickUp");
        this.preventFromHoppers = config.getBoolean("preventFromHoppers");
        this.clearProtectionOnLeave = config.getBoolean("clearProtectionOnLeave");
        this.clearProtectionOnDeath = config.getBoolean("clearProtectionOnDeath");

        this.timeToProtect = Math.round(config.getDouble("timeToProtect"));
    }

    public void checkForDependencies() {
        if (getMythicMobsSupport()) {
            Bukkit.getLogger().log(Level.INFO, "[RPGDrop] MythicMobs support enabled.");
            if (!isMythicEventsRegistered()) {
                Bukkit.getPluginManager().registerEvents(new EventsMythic(), RPGDrop.plugin);
                this.mythicEventsRegistered = true;
                Bukkit.getLogger().log(Level.INFO, "[RPGDrop] MythicMobs events registered.");
            }
        }
        if (getMMOCoreSupport()) {
            Bukkit.getLogger().log(Level.INFO, "[RPGDrop] MMOCore support enabled.");
        }
        if (getSimpleClansSupport()) {
            Bukkit.getLogger().log(Level.INFO, "[RPGDrop] SimpleClans support enabled.");
        }
        if (getPartiesSupport()) {
            Bukkit.getLogger().log(Level.INFO, "[RPGDrop] Parties support enabled.");
        }
    }

    public boolean getMythicMobsSupport() {
        return mythicMobsSupport;
    }
    public boolean isMythicEventsRegistered() {
        return mythicEventsRegistered;
    }
    public boolean getMMOCoreSupport() {
        return mmoCoreSupport;
    }

    public boolean getMMOCorePartiesSupport() {
        return mmocorePartiesSupport;
    }

    public boolean getMMOCoreGuildsSupport() {
        return mmocoreGuildsSupport;
    }

    public boolean getMMOCoreFriendsSupport() {
        return mmocoreFriendsSupport;
    }

    public boolean getSimpleClansSupport() {
        return simpleClansSupport;
    }
    public boolean getSimpleClansAlliedClansShareLoot() {
        return simpleClansAlliedClansShareLoot;
    }

    public boolean getPartiesSupport() {
        return partiesSupport;
    }

    public boolean getMobCanPickUp() {
        return mobCanPickUp;
    }

    public boolean getPreventFromHoppers() {
        return preventFromHoppers;
    }

    public boolean getClearProtectionOnLeave() {
        return clearProtectionOnLeave;
    }

    public boolean getClearProtectionOnDeath() {
        return clearProtectionOnDeath;
    }

    public long getTimeToProtect() {
        return timeToProtect;
    }
}
