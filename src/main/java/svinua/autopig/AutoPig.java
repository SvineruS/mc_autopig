package svinua.autopig;

import com.destroystokyo.paper.entity.Pathfinder;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AutoPig {
    static HashMap<UUID, AutoPig> pigs = new HashMap<UUID, AutoPig>();

    Inventory inv;
    Player owner;
    Pig pig;
    Location home;

    AutoPig (Pig pig, Player player) {
        this.pig = pig;
        owner = player;
        inv = Bukkit.createInventory(null, 9, "Auto Pig");
        save_home();
        make_cool();
    }

    static AutoPig get(Pig pig, Player player) {
        if (pigs.containsKey(pig.getUniqueId()))
            return pigs.get(pig.getUniqueId());
        return new AutoPig(pig, player);
    }


    void make_cool() {
        pig.setCustomName("Auto Pig (" + owner.getName() + ")");
        pig.setGlowing(true);
        //        pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
        pig.setRemoveWhenFarAway(false);
    }

    void save_home() {
        home = pig.getLocation();
    }

    void go_home() {
        pig.getPathfinder().moveTo(home, 2);
    }

    void quarry() {
        LocalSession session = Main.worldEditPlugin.getSession(owner);
        Region sel = null;
        try {
            sel = session.getSelection(session.getSelectionWorld());
        } catch (NullPointerException | IncompleteRegionException e) {
            owner.sendMessage("please select region");
            e.printStackTrace();
            return;
        }

        owner.sendMessage("mining..");

        ArrayList<Block> blocks = new ArrayList<>();
        for (BlockVector3 b: sel) {
            Block block = new Location(owner.getWorld(), b.getX(), b.getY(), b.getZ()).getBlock();
            if (block.isEmpty() || block.isLiquid())
                continue;
            blocks.add(block);
        }
//        blocks.sort((a, b) -> b.getY() - a.getY());
        mine(blocks);
    }

    void mine(ArrayList<Block> blocks) {
        if (blocks.size() == 0) {
            owner.sendMessage("i`m done");
            return;
        }
        Block block = blocks.get(blocks.size()-1);

        Pathfinder.PathResult path = pig.getPathfinder().findPath(block.getLocation());
        double dist;
        try {
            dist = path.getFinalPoint().toVector().distance(block.getLocation().toVector());
            if (path.getPoints().size() < 2 && dist > 2)
                throw new NullPointerException();
        } catch (NullPointerException e) {
            owner.sendMessage("help! i stuck");
            return;
        }

        if (dist > 2) {
            pig.getPathfinder().moveTo(path);
        } else {
            block.breakNaturally();
            blocks.remove(blocks.size()-1);
        }

        new BukkitRunnable() {public void run() {
            mine(blocks);
        }}.runTaskLater(Main.instance, (long) (20*1));
    }



}
