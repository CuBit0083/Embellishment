package org.cubit.embellishment.api;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IEmbellishmentType {

    String getName();
    ItemStack getItem();
    List<String> getLore();
    short getDurability();
    boolean isTeleport();
}
