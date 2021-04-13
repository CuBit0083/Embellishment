package org.cubit.embellishment;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cubit.embellishment.api.IEmbellishmentType;
import org.cubit.embellishment.embellishment.EmbellishmentManager;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;

import java.util.*;

public class EmbellishmentUIManager {

    private EmbellishmentTypeManager embellishmentTypeManager;
    private EmbellishmentManager embellishmentManager;
    private Map<UUID, Inventory> playerUiMap;

    public EmbellishmentUIManager(EmbellishmentTypeManager embellishmentTypeManager, EmbellishmentManager embellishmentManager) {
        this.embellishmentTypeManager = embellishmentTypeManager;
        this.embellishmentManager = embellishmentManager;
        this.playerUiMap = new HashMap<>();
    }

    public void openInventory(Player player) {
        this.playerUiMap.remove(player.getUniqueId());
        if (!this.playerUiMap.containsKey(player.getUniqueId())) {
            this.playerUiMap.put(player.getUniqueId(), Bukkit.createInventory(null, 54, EmbellishmentCore.suffix + "" + player.getName() + "님의 치장 목록"));
            if (this.embellishmentManager.getPlayerEmbellishmentTypeMap().containsKey(player.getUniqueId())) {
                this.addItem(player, Material.EMERALD, 49, "치장 적용", Collections.singletonList("§f" + this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(player.getUniqueId()).getName() + " 치장"));
            }else {
                this.addItem(player, Material.EMERALD, 49, "치장 적용", Collections.singletonList("§f적용된 치장이 없습니다."));
            }
        }
        for (String name : this.embellishmentTypeManager.getEmbellishmentType().keySet()) {
            IEmbellishmentType type = this.embellishmentTypeManager.getEmbellishmentType(name);
            if (this.embellishmentManager.getEmbellishment(player.getUniqueId(), name) != null) {
                this.addItem(player, type.getItem().getType(), type.getName(), type.getLore(), type.getDurability() , false);
                continue;
            }
            this.addItem(player, Material.BARRIER, type.getName(), Collections.singletonList("§f미보유 치장 입니다."));
        }
        player.openInventory(this.playerUiMap.get(player.getUniqueId()));
    }

    public void addItem(Player player, Material material, String name, List<String> lore, short durability , boolean b) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setDurability(durability);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§f" + name);
        itemMeta.setLore(lore);
        if(b) {
            itemMeta.addEnchant(Enchantment.LURE, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        this.playerUiMap.get(player.getUniqueId()).addItem(itemStack);
    }

    public void addItem(Player player, Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§f" + name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.playerUiMap.get(player.getUniqueId()).addItem(itemStack);
    }

    public void addItem(Player player, Material material, int i , String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§f" + name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.playerUiMap.get(player.getUniqueId()).setItem(i , itemStack);
    }

}