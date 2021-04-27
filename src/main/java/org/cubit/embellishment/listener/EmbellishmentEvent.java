package org.cubit.embellishment.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.cubit.embellishment.EmbellishmentCore;
import org.cubit.embellishment.embellishment.EmbellishmentManager;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;
import org.cubit.embellishment.util.EmbellishmentScheduler;


public class EmbellishmentEvent implements Listener {

    private EmbellishmentManager embellishmentManager;
    private EmbellishmentTypeManager embellishmentTypeManager;
    private EmbellishmentScheduler embellishmentScheduler;

    public EmbellishmentEvent(EmbellishmentManager embellishmentManager, EmbellishmentTypeManager embellishmentTypeManager, EmbellishmentScheduler embellishmentScheduler) {
        this.embellishmentManager = embellishmentManager;
        this.embellishmentTypeManager = embellishmentTypeManager;
        this.embellishmentScheduler = embellishmentScheduler;
    }

    @EventHandler
    public void onEmbellishmentEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.embellishmentManager.getPlayerEmbellishmentTypeMap().containsKey(player.getUniqueId())) {
            this.embellishmentManager.getArmorStandMap().put(player.getUniqueId(), this.embellishmentManager.getCreateArmorStand(player));
            ItemStack itemStack = this.embellishmentManager.getEmbellishment(player.getUniqueId(), this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(player.getUniqueId()).getName()).getItem();
            itemStack.setDurability(this.embellishmentManager.getEmbellishment(player.getUniqueId(), this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(player.getUniqueId()).getName()).getDurability());
            this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()).setHelmet(itemStack);
            this.embellishmentScheduler.onScheduler(player, this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(player.getUniqueId()).getName(), this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()));
        }
    }

    @EventHandler
    public void onEmbellishmentEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()) != null) {
            this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()).remove();
            this.embellishmentManager.getArmorStandMap().remove(player.getUniqueId());
        }
    }
    @EventHandler
    public void onEmbellishmentEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase(EmbellishmentCore.suffix + "" + player.getName() + "님의 치장 목록")) {
            event.setCancelled(true);
            String name = event.getCurrentItem().getItemMeta().getDisplayName();
            name = name.replaceAll("§f", "");

            if (this.embellishmentManager.getEmbellishment(player.getUniqueId(), name) != null) {
                if (this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(player.getUniqueId()) != null) {
                    if (this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(player.getUniqueId()).equals(this.embellishmentTypeManager.getEmbellishmentType(name))) {
                        this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()).remove();
                        this.embellishmentManager.getArmorStandMap().remove(player.getUniqueId());
                        this.embellishmentManager.getPlayerEmbellishmentTypeMap().remove(player.getUniqueId());
                        player.sendMessage(EmbellishmentCore.suffix + "치장이 해제되었습니다.");
                        player.closeInventory();
                        return;
                    }
                }
                this.embellishmentManager.addEmbellishment(player.getUniqueId(), this.embellishmentManager.getEmbellishment(player.getUniqueId(), name));
                if (this.embellishmentManager.getArmorStandMap().containsKey(player.getUniqueId())) {
                    this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()).remove();
                    this.embellishmentManager.getArmorStandMap().remove(player.getUniqueId());
                }
                this.embellishmentManager.getArmorStandMap().put(player.getUniqueId(), this.embellishmentManager.getCreateArmorStand(player));
                ItemStack itemStack = this.embellishmentManager.getEmbellishment(player.getUniqueId(), name).getItem();
                itemStack.setDurability(this.embellishmentManager.getEmbellishment(player.getUniqueId(), name).getDurability());
                this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()).setHelmet(itemStack);
                player.setPassenger(this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()));
                this.embellishmentManager.getPlayerEmbellishmentTypeMap().put(player.getUniqueId(), this.embellishmentTypeManager.getEmbellishmentType(name));
                this.embellishmentScheduler.onScheduler(player, name, this.embellishmentManager.getArmorStandMap().get(player.getUniqueId()));
                player.sendMessage(EmbellishmentCore.suffix + "정상적으로 치장이 등록되었습니다.");
                player.playSound(player.getLocation() , Sound.ENTITY_PLAYER_LEVELUP , 1 ,1);
                player.closeInventory();

            }
        }

    }


}
