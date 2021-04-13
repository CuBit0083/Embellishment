package org.cubit.embellishment.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubit.embellishment.embellishment.EmbellishmentManager;
import org.cubit.embellishment.embellishment.EmbellishmentType;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;;

import java.io.File;
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
            int i = 0;
            for (String name : this.embellishmentManager.getPlayerEmbellishments().get(uuid).keySet()) {
                saveFile.set("Data." + uuid + "." + i, this.embellishmentManager.getEmbellishment(uuid, name).getName());
                i++;

            }
            if (this.embellishmentManager.getPlayerEmbellishmentTypeMap().containsKey(uuid)) {
                saveFile.set("Embellishment." + uuid , this.embellishmentManager.getPlayerEmbellishmentTypeMap().get(uuid).getName());
            }
            saveFile.save(file);
        }
    }


    public void loadEmbellishmentConfig() {
        File[] files = new File(plugin.getDataFolder() + "/saveData").listFiles();
        if (files == null) return;
        for (File file : files) {
            FileConfiguration saveFile = YamlConfiguration.loadConfiguration(file);
            for (String key : saveFile.getConfigurationSection("Data").getKeys(false)) {
                for (String name : saveFile.getConfigurationSection("Data." + key).getKeys(false)) {
                    this.embellishmentManager.addEmbellishment(UUID.fromString(key) , this.embellishmentTypeManager.getEmbellishmentType(saveFile.getString("Data." + key + "." + name)));
                }
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