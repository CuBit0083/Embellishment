package org.cubit.embellishment.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubit.embellishment.embellishment.EmbellishmentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmbellishmentScheduler {

    private JavaPlugin plugin;
    private EmbellishmentManager embellishmentManager;
    private Map<UUID, Integer> scheduler;

    public EmbellishmentScheduler(JavaPlugin plugin, EmbellishmentManager embellishmentManager) {
        this.plugin = plugin;
        this.embellishmentManager = embellishmentManager;
        this.scheduler = new HashMap<>();
    }

    public void onScheduler(Player player , String name , ArmorStand armorStand) {
        if (this.embellishmentManager.getEmbellishment(player.getUniqueId(), name).isTeleport()) {
            this.scheduler.put(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                armorStand.teleport(player.getEyeLocation());
            }, 0, 1));
        }else {
            this.scheduler.put(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                player.setPassenger(armorStand);
                armorStand.teleport(player.getEyeLocation());
            }, 0, 1));
        }
    }
}
