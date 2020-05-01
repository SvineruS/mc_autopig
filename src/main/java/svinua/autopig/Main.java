package svinua.autopig;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static WorldEditPlugin WEPlugin;
    public static Main instance;
    public static RegionQuery WGRegionQuery;

    static {
        ConfigurationSerialization.registerClass(AutoPig.class, "AutoPig");
    }

    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        WEPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            WGRegionQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        }


        loadPigs();
    }

    public void onDisable() {
        savePigs();
    }


    public void loadPigs() {
        AutoPig.PIGS = (HashMap) getConfig().getConfigurationSection("pigs").getValues(false);
    }


    public void savePigs() {
        this.getConfig().createSection("pigs", AutoPig.PIGS);
        this.saveConfig();
    }



}
