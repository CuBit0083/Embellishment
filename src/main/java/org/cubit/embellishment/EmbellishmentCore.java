package org.cubit.embellishment;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubit.embellishment.api.IEmbellishmentType;
import org.cubit.embellishment.comand.EmbellishmentCommand;
import org.cubit.embellishment.config.EmbellishmentConfig;
import org.cubit.embellishment.config.EmbellishmentTypeConfig;
import org.cubit.embellishment.embellishment.EmbellishmentManager;
import org.cubit.embellishment.embellishment.EmbellishmentTypeManager;
import org.cubit.embellishment.listener.EmbellishmentEvent;
import org.cubit.embellishment.util.EmbellishmentScheduler;
import skywolf46.commandannotation.CommandAnnotation;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmbellishmentCore extends JavaPlugin {

    public final static String suffix = "§f[ §bSystem §f] ";
    private Logger logger;
    private EmbellishmentTypeManager embellishmentTypeManager;
    private EmbellishmentManager embellishmentManager;
    private EmbellishmentTypeConfig embellishmentTypeConfig;
    private EmbellishmentConfig embellishmentConfig;

    private EmbellishmentCommand embellishmentCommand;
    private EmbellishmentUIManager embellishmentUIManager;
    private EmbellishmentEvent embellishmentEvent;
    private EmbellishmentScheduler embellishmentScheduler;

    @Override
    public void onEnable() {
        this.logger = this.getLogger();
        this.embellishmentManager = new EmbellishmentManager();
        this.embellishmentTypeManager = new EmbellishmentTypeManager();
        this.embellishmentTypeConfig = new EmbellishmentTypeConfig(this, this.embellishmentTypeManager);
        this.embellishmentConfig = new EmbellishmentConfig(this, this.embellishmentManager , this.embellishmentTypeManager);
        this.embellishmentUIManager = new EmbellishmentUIManager(embellishmentTypeManager , embellishmentManager);
        this.embellishmentScheduler = new EmbellishmentScheduler(this , this.embellishmentManager);
        this.embellishmentEvent = new EmbellishmentEvent(this.embellishmentManager , this.embellishmentTypeManager ,this.embellishmentScheduler);
        this.embellishmentCommand = new EmbellishmentCommand(this.embellishmentManager ,this.embellishmentTypeManager , this.embellishmentTypeConfig , this.embellishmentUIManager);

        CommandAnnotation.init(this);
        CommandAnnotation.registerCommandObject(this.embellishmentCommand);
        try {
            if (this.embellishmentTypeConfig.getEmbellishmentTypes() != null) {
                List<IEmbellishmentType> iEmbellishmentTypes = this.embellishmentTypeConfig.getEmbellishmentTypes();
                for (IEmbellishmentType iEmbellishmentType : iEmbellishmentTypes) {
                    this.logger.log(Level.INFO, iEmbellishmentType.getName() + " , " + iEmbellishmentType.getItem() + " , " + iEmbellishmentType.getDurability() + " , " + iEmbellishmentType.getLore() + " " + iEmbellishmentType.isTeleport());

                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.embellishmentConfig.loadEmbellishmentConfig();
        Bukkit.getPluginManager().registerEvents(this.embellishmentEvent , this);
    }

    @Override
    public void onDisable() {
        try {
            this.embellishmentConfig.saveEmbellishmentConfig();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        for (UUID uuid : this.embellishmentManager.getArmorStandMap().keySet()) {
            this.embellishmentManager.getArmorStandMap().get(uuid).remove();
        }
        this.embellishmentManager.getArmorStandMap().clear();
        System.out.println("1");
    }

}
