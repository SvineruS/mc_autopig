package svinua.autopig;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    public static WorldEditPlugin WEPlugin;
    public static Main instance;
    public static RegionQuery WGRegionQuery;

    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        WEPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            WGRegionQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        }

    }

    public void onDisable() {
    }

    @EventHandler
    public final void interact(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if (!(e.getRightClicked() instanceof Pig))
            return;

        e.setCancelled(true);
        AutoPig pig = AutoPig.get((Pig) e.getRightClicked(), p);

        pig.quarry();
    }

}
