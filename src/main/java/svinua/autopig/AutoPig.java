package svinua.autopig;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import svinua.autopig.Feature.Feature;
import svinua.autopig.Feature.FeatureIdle;
import svinua.autopig.Feature.FeatureMining;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public class AutoPig implements InventoryHolder{
    static HashMap<UUID, AutoPig> pigs = new HashMap<>();

    public Inventory inv;
    public Player owner;
    public Pig pig;
    public Feature state = new FeatureIdle(this);

    AutoPig (Pig pig, Player player) {
        this.pig = pig;
        owner = player;
        inv = Bukkit.createInventory(this, 27, "Auto Pig");
        inventory();
        make_cool();
    }

    static AutoPig get(Pig pig, Player player) {
        if (is_autopig(pig)) return pigs.get(pig.getUniqueId());
        return set_autopig(pig, player);
    }

    public static boolean is_autopig(Pig pig) {
        return (pigs.containsKey(pig.getUniqueId()));
    }

    public static AutoPig set_autopig(Pig pig, Player player) {
        AutoPig autoPig = new AutoPig(pig, player);
        pigs.put(pig.getUniqueId(), autoPig);
        return autoPig;
    }


    void make_cool() {
        set_name();
        pig.setGlowing(true);
        //        pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
        pig.setRemoveWhenFarAway(false);
    }

    void inventory() {
        inv.setItem(26, Static.INSTRUCTION);
        inv.setItem(18, Static.MINING);
        inv.setItem(19, Static.FISHING);
        inv.setItem(20, Static.DEFENCE);
        inv.setItem(21, Static.RIDE);

    }

    public void set_state(Class<? extends Feature> new_state_class) {
        if (state.getClass() == new_state_class) {
            owner.sendMessage("already this state");
            return;
        }

        try {
            state = new_state_class.getConstructor(AutoPig.class).newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}

        set_name();

    }


    void mine() {
        set_state(FeatureMining.class);
    }

    public void set_name() {
        pig.setCustomName("Auto Pig (" + owner.getName() + ") - " + state.get_name());
    }



    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void menu_click(ItemStack item) {
        if (item.isSimilar(Static.MINING)) mine();
        // todo
//        if (item.isSimilar(Static.FISHING)) mine();
//        if (item.isSimilar(Static.DEFENCE)) mine();
//        if (item.isSimilar(Static.RIDE)) mine();
//        if (item.isSimilar(Static.INSTRUCTION)) mine();
    }
}
