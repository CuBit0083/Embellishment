package org.cubit.embellishment.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubit.embellishment.api.IEmbellishmentType;
import org.cubit.embellishment.embellishment.EmbellishmentManager;
import org.cubit.embellishment.embellishment.EmbellishmentType;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EmbellishmentConfig {

    private JavaPlugin plugin;
    private EmbellishmentManager embellishmentManager;
    private EmbellishmentTypeManager embellishmentTypeManager;

    public EmbellishmentConfig(JavaPlugin plugin, EmbellishmentManager embellishmentManager, EmbellishmentTypeManager embellishmentTypeManager) {
        this.plugin = plugin;
        this.embellishmentManager = embellishmentManager;
        this.embellishmentTypeManager = embellishmentTypeManager;
    }

    public void saveEmbellishmentConfig() throws Exception {
        for (UUID uuid : this.embellishmentManager.getPlayerEmbellishments().keySet()) {
            File file = new File(plugin.getDataFolder(), "saveData/" + uuid + ".yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileConfiguration saveFile = YamlConfiguration.loadConfiguration(file);
            Collection<IEmbellishmentType> types = this.embellishmentManager.getPlayerEmbellishments().get(uuid).values();
            List<String> list = new ArrayList<>();
            for (IEmbellishmentType iEmbellishmentType : types) {
                list.add(iEmbellishmentType.getName());
            }
            saveFile.set("Data." + uuid, list);
            if (this.embellishmentManager.getPlayerEmbellishmentTypeMap().containsKey(uuid)) {
                saveFile.set("Embellishment." + uuid, this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(uuid).getName());
            }
            saveFile.save(file);
        }
    }


    public void loadEmbellishmentConfig() {
        File[] files = new File(plugin.getDataFolder() + "/saveData").listFiles();
        if (files == null) return;
        for (File file : files) {
            FileConfiguration saveFile = YamlConfiguration.loadConfiguration(file);
            String uuid = file.getName().replace(".yml" , "");
            List<String> list = saveFile.getConfigurationSection("Data").getStringList(uuid);
            for (String s : list) {
                this.embellishmentManager.addEmbellishment(UUID.fromString(uuid), this.embellishmentTypeManager.getEmbellishmentType(s));
            }


            if (saveFile.getConfigurationSection("Embellishment") != null) {
                for (String key : saveFile.getConfigurationSection("Embellishment").getKeys(false)) {
                    this.embellishmentManager.getPlayerEmbellishmentTypeMap().put(UUID.fromString(key), this.embellishmentTypeManager.getEmbellishmentType(saveFile.getString("Embellishment." + key)));

                }
            }
            file.delete();

        }
    }
}