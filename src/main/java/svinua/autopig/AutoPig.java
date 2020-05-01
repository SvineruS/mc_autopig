package svinua.autopig;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
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



@SerializableAs("AutoPig")
public class AutoPig implements InventoryHolder, ConfigurationSerializable {
    static HashMap<String, AutoPig> PIGS = new HashMap<>();

    public Pig pig;
    public Player owner;
    public Inventory inv = Bukkit.createInventory(this, 27, "Auto Pig");
    public Feature state = new FeatureIdle(this);



    AutoPig (Pig pig, Player player) {
        this.pig = pig;
        this.owner = player;
        put_menu_in_inventory();
    }

    public static AutoPig create(Pig pig, Player player) {
        AutoPig autoPig = new AutoPig(pig, player);
        PIGS.put(pig.getUniqueId().toString(), autoPig);

        autoPig.set_name();
        pig.setGlowing(true);
        pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(2);
        pig.setRemoveWhenFarAway(false);

        return autoPig;
    }


    static AutoPig get_autopig(Pig pig) {
        return PIGS.get(pig.getUniqueId().toString());
    }
    public static boolean is_autopig(Pig pig) {
        return (PIGS.containsKey(pig.getUniqueId().toString()));
    }



    public void say_to_owner(String text) {
        owner.sendMessage("[AutoPig]" + text);
    }



    public void menu_click(ItemStack item) {
        if (item.isSimilar(Static.MINING))  set_state(FeatureMining.class);
        // todo
//        if (item.isSimilar(Static.FISHING)) mine();
//        if (item.isSimilar(Static.DEFENCE)) mine();
//        if (item.isSimilar(Static.RIDE)) mine();
//        if (item.isSimilar(Static.INSTRUCTION)) mine();
    }

    public void set_state(Class<? extends Feature> new_state_class) {
        if (state.getClass() == new_state_class) {
            say_to_owner("already this state");
            return;
        }
        try {
            state = new_state_class.getConstructor(AutoPig.class).newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}

        set_name();
    }

    void set_name() {
        pig.setCustomName("Auto Pig (" + owner.getName() + ") - " + state.get_name());
    }

    void put_menu_in_inventory() {
        inv.setItem(26, Static.INSTRUCTION);
        inv.setItem(18, Static.MINING);
        inv.setItem(19, Static.FISHING);
        inv.setItem(20, Static.DEFENCE);
        inv.setItem(21, Static.RIDE);
    }


    @Override
    public Inventory getInventory() {
        return inv;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<ItemStack> inv_items = new ArrayList<>();
        for (int i=0; i<18; i++)
            inv_items.add(this.inv.getItem(i));
        map.put("pig", pig.getUniqueId().toString());
        map.put("owner", owner.getUniqueId().toString());
        map.put("inv", inv_items.toArray());
        return map;
    }

    public static AutoPig deserialize(Map<String, Object> map) {
        Pig pig = (Pig) Bukkit.getEntity(UUID.fromString((String) map.get("pig")));
        Player owner = (Player) Bukkit.getEntity(UUID.fromString((String) map.get("owner")));
        AutoPig autopig = new AutoPig(pig, owner);
        ArrayList<ItemStack> inv_items = (ArrayList<ItemStack>) map.get("inv");
        for (int i=0; i<18; i++)
            autopig.inv.setItem(i, inv_items.get(i));

        return autopig;

    }

}
