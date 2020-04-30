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

    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        WEPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            WGRegionQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        }


//    this.getConfig().setDefaults();
//
        ConfigurationSerialization.registerClass(AutoPig.class);
        this.getConfig();
        System.out.println(this.getConfig().get("pigs"));
        System.out.println(this.getConfig().get("pigs2"));
        Map<String, Object> pigs = this.getConfig().getConfigurationSection("pigs").getValues(false);
        System.out.println(pigs.size() + "");
        System.out.println(this.getConfig().getString("chlen"));
        // todo
        // load autopigs

    }

    public void onDisable() {
        this.getConfig().createSection("pigs", AutoPig.PIGS);
        this.getConfig().set("chlen", "a");
        System.out.println( AutoPig.PIGS + "");

        this.saveConfig();

        // todo
        // save autopigs

    }




}
