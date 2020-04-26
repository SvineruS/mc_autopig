package svinua.autopig.Feature;

import com.destroystokyo.paper.entity.Pathfinder;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import svinua.autopig.AutoPig;
import svinua.autopig.Main;

import java.util.ArrayList;
import java.util.Comparator;

public class FeatureMining extends Feature {


    final int TIME_TO_MOVE = 50 * 5;
    final int RANGE_TO_MINE = 2;

    ItemStack pickaxe;

    public FeatureMining(AutoPig pig) {
        super(pig);
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, this::start);
    }

    public String get_name() {
        return "Mining";
    }




    void start() {

        try {
            pickaxe = find_pickaxe();
            if (pickaxe == null)
                throw new Exception("where is my pickaxe blyad");

            Region selection = get_selection();
            if (selection == null)
                throw new Exception("please select region");

            pig.owner.sendMessage("mining...");
            // todo set started

            while (!stopped) {
                ArrayList<Block> blocks = get_blocks(selection);
                if (blocks.isEmpty())
                    break;

                try_mine_block(blocks.get(0));
            }

            pig.owner.sendMessage("i`m done");

        } catch (Exception e) {
            pig.owner.sendMessage(e.getMessage() + " " + e.getCause() + " " + e);
        }
        stop();

    }


    Region get_selection() {
        LocalSession session = Main.WEPlugin.getSession(pig.owner);
        try {
            return session.getSelection(session.getSelectionWorld());
        } catch (NullPointerException | IncompleteRegionException e) {
            return null;
        }
    }

    ArrayList<Block> get_blocks(Region sel) throws Exception {
        ArrayList<Block> blocks = new ArrayList<>();

        for (BlockVector3 b : sel) {

            com.sk89q.worldedit.util.Location WElocation = new com.sk89q.worldedit.util.Location(sel.getWorld(), b.toVector3());
            if (!check_wg_perms(WElocation))
                throw new Exception("region has blocks that pig doesn't have permissions to mine");

            Block block = new Location(pig.pig.getWorld(), b.getX(), b.getY(), b.getZ()).getBlock();
            if (!check_need_mine(block)) continue;

            blocks.add(block);
        }

        blocks.sort(Comparator.comparing(block -> block.getLocation().distance(pig.pig.getLocation()) - block.getY() * 1000));
        return blocks;
    }


    void try_mine_block(Block block) throws Exception {
        if (is_pickaxe_broken()) throw new Exception("pickaxe nearly break");
        if (is_inventory_full()) throw new Exception("inventory full");

        for (int i = 0; i < 100; i++) {  // 100 attempts to move to block
            if (pig.pig.getLocation().distance(block.getLocation()) < RANGE_TO_MINE) break;
            if (!Bukkit.getScheduler().callSyncMethod(Main.instance, () -> move_to_block(block)).get()) continue;
            Thread.sleep(TIME_TO_MOVE);
        }

        Thread.sleep((long) get_block_break_time(block));
        if (Bukkit.getScheduler().callSyncMethod(Main.instance, () -> block.breakNaturally(pickaxe, true)).get())
            damage_pickaxe();
    }



    boolean move_to_block(Block block) {
        Pathfinder.PathResult path = pig.pig.getPathfinder().findPath(block.getLocation());
        if (path == null)
            return false;
        if (path.getFinalPoint().toVector().distance(block.getLocation().toVector()) > RANGE_TO_MINE)
            return false;

        pig.pig.getPathfinder().moveTo(path);
        return true;
    }


    boolean check_wg_perms(com.sk89q.worldedit.util.Location location) {
        if (Main.WGRegionQuery == null)
            return true;
        return Main.WGRegionQuery.testState(location, WorldGuardPlugin.inst().wrapPlayer(pig.owner), Flags.BUILD);
    }

    boolean check_need_mine(Block block) {
        return !block.isEmpty() && !block.isLiquid() && can_safe_break_block(block);
    }

    boolean can_safe_break_block(Block block) {
        return !(block.getType().toString().toLowerCase().contains("ore") &&
                block.getDrops(pickaxe, pig.owner).isEmpty());
    }

    ItemStack find_pickaxe() {
        ItemStack[] items = pig.inv.getContents();
        for (int i=0; i<9*2; i++) {
            ItemStack item = items[i];
            if (item == null) continue;
            if (item.getType().toString().toLowerCase().contains("pickaxe")) return item;
        }
        return null;
    }

    boolean is_pickaxe_broken() {
        return ((Damageable) pickaxe.getItemMeta()).getDamage() >= pickaxe.getType().getMaxDurability() - 5;// i hate new bukkit
    }

    void damage_pickaxe() {
        Damageable meta = (Damageable) pickaxe.getItemMeta();
        meta.setDamage(meta.getDamage() + 1);
        pickaxe.setItemMeta((ItemMeta) meta);
    }


    float get_block_break_time(Block block) {
        return block.getType().getHardness() * 1.5f / get_speed(pickaxe) * 1000;
    }

    float get_speed(ItemStack item) {
        float speed = 1;

        String mat = item.getType().toString().toLowerCase();
        if (mat.contains("wood")) speed = 2;
        else if (mat.contains("stone")) speed = 4;
        else if (mat.contains("iron")) speed = 6;
        else if (mat.contains("diamond")) speed = 8;
        else if (mat.contains("netherite")) speed = 9;
        else if (mat.contains("gold")) speed = 12;

        int eff = item.getEnchantments().getOrDefault(Enchantment.DIG_SPEED, 0);
        if (eff > 0)
            speed += eff * 2 + 1;

        // todo check potions

        return speed;
    }



    boolean is_inventory_full() {
        return false;
        // todo
    }

}
