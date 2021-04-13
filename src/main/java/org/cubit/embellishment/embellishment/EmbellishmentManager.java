package org.cubit.embellishment.embellishment;

import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.cubit.embellishment.api.IEmbellishmentType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class EmbellishmentManager {

    private Map<UUID , Inventory> playerUiMap;
    private Map<UUID , IEmbellishmentType> playerEmbellishmentTypeMap;
    private Map<UUID , ArmorStand> armorStandMap;
    private Map<UUID, Map<String, IEmbellishmentType>> playerEmbellishments;


    public EmbellishmentManager() {
        this.playerUiMap = new HashMap<>();
        this.playerEmbellishments = new HashMap<>();
        this.playerEmbellishmentTypeMap = new HashMap<>();
        this.armorStandMap = new HashMap<>();

    }
    public void addEmbellishment(UUID uuid, IEmbellishmentType iEmbellishmentType) {
        if (playerEmbellishments.get(uuid) == null) {
            this.playerEmbellishments.put(uuid, new HashMap<>());
            this.playerEmbellishments.get(uuid).put(iEmbellishmentType.getName(), iEmbellishmentType);

        } else {
            this.playerEmbellishments.get(uuid).put(iEmbellishmentType.getName(), iEmbellishmentType);
        }
    }

    public void removeEmbellishment(UUID uuid, String name) {
        this.playerEmbellishments.get(uuid).remove(name);
    }

    public IEmbellishmentType getEmbellishment(UUID uuid, String name) {
        if (playerEmbellishments.get(uuid) == null) {
            this.playerEmbellishments.put(uuid, new HashMap<>());
        }
        return this.playerEmbellishments.get(uuid).get(name);
    }

    public ArmorStand getCreateArmorStand(final Player player) {
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation() , EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvulnerable(true);
        armorStand.setMarker(true);
        armorStand.setSmall(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        return armorStand;
    }

}
