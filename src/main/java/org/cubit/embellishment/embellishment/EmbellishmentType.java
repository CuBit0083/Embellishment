package org.cubit.embellishment.embellishment;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.cubit.embellishment.api.IEmbellishmentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EmbellishmentType implements IEmbellishmentType  {

    private String name;
    private ItemStack item;
    private List<String> lore;
    private short durability;
    private boolean isTeleport;


    public EmbellishmentType(String name, Material item, short durability, List<String> lore , boolean isTeleport) {
        this.name = name;
        this.item = new ItemStack(item);
        this.durability = durability;
        this.lore = lore;
        this.isTeleport = isTeleport;
    }

}
