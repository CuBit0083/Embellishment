package org.cubit.embellishment.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubit.embellishment.api.IEmbellishmentType;
import org.cubit.embellishment.embellishment.EmbellishmentType;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EmbellishmentTypeConfig {

    private JavaPlugin plugin;
    private EmbellishmentTypeManager embellishmentTypeManager;

    public EmbellishmentTypeConfig(JavaPlugin plugin, EmbellishmentTypeManager embellishmentTypeManager) {
        this.plugin = plugin;
        this.embellishmentTypeManager = embellishmentTypeManager;
    }

    public List<IEmbellishmentType> getEmbellishmentTypes() throws Exception {
        File file = new File(plugin.getDataFolder(), "Embellishment/Embellishment.yml");
        List<IEmbellishmentType> iEmbellishmentTypes = new ArrayList<>();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileConfiguration saveFile = YamlConfiguration.loadConfiguration(file);
        if (saveFile.getConfigurationSection("Embellishment") == null) {
            plugin.getLogger().log(Level.WARNING , "Embellishment 인식된 파일이 없어서 가져오기를 종료 합니다.");
            return null;
        }
            for (String key : saveFile.getConfigurationSection("Embellishment").getKeys(false)) {
                IEmbellishmentType embellishmentType = new EmbellishmentType(key, Material.valueOf(saveFile.getString("Embellishment." + key + ".ITEM"))
                        , (short) saveFile.getInt("Embellishment." + key + ".Durability"), saveFile.getStringList("Embellishment." + key + ".LORE"), saveFile.getBoolean("Embellishment." + key + ".Teleport"));
                this.embellishmentTypeManager.register(key, embellishmentType);
                iEmbellishmentTypes.add(embellishmentType);

            }
        return iEmbellishmentTypes;
    }


}
