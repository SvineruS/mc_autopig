package svinua.autopig;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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


        // todo
        // load autopigs

    }

    public void onDisable() {
        // todo
        // save autopigs

    }




}
