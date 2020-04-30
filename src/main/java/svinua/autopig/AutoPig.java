package svinua.autopig;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import svinua.autopig.Feature.Feature;
import svinua.autopig.Feature.FeatureIdle;
import svinua.autopig.Feature.FeatureMining;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AutoPig implements InventoryHolder, ConfigurationSerializable {
    static HashMap<String, AutoPig> PIGS = new HashMap<>();

    public Pig pig;
    public Player owner;
    public Inventory inv = Bukkit.createInventory(this, 27, "Auto Pig");
    public Feature state = new FeatureIdle(this);

    AutoPig(Map<String, Object> map) {
//        this.pig = (Pig) Bukkit.getEntity(UUID.fromString((String) map.get("pig")));
//        this.owner = (Player) Bukkit.getEntity(UUID.fromString((String) map.get("owner")));
//        ItemStack[] inv_items = (ItemStack[]) map.get("inv");
//        for (int i=0; i<18; i++)
//            this.inv.setItem(i, inv_items[i]);

    }

    AutoPig (Pig pig, Player player) {
        this.pig = pig;
        this.owner = player;
//        inventory();
//        make_cool();
    }

    static AutoPig get(Pig pig, Player player) {
        if (is_autopig(pig)) return PIGS.get(pig.getUniqueId().toString());
        return set_autopig(pig, player);
    }

    public static boolean is_autopig(Pig pig) {
        return (PIGS.containsKey(pig.getUniqueId().toString()));
    }

    public static AutoPig set_autopig(Pig pig, Player player) {
        AutoPig autoPig = new AutoPig(pig, player);
        PIGS.put("chlen", autoPig);
//        PIGS.put(pig.getUniqueId().toString(), autoPig);
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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        ArrayList<ItemStack> inv_items = new ArrayList<>();
        for (int i=0; i<18; i++)
            inv_items.add(this.inv.getItem(i));
//        stringObjectHashMap.put("inv", inv_items.toArray());
        stringObjectHashMap.put("owner", owner.getUniqueId().toString());
        stringObjectHashMap.put("pig", pig.getUniqueId().toString());
//        stringObjectHashMap.put("test", new ItemStack(Material.AIR));
        return stringObjectHashMap;
    }
}
