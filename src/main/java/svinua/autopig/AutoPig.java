package svinua.autopig;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import svinua.autopig.Feature.Feature;
import svinua.autopig.Feature.FeatureIdle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@SerializableAs("AutoPig")
public class AutoPig implements ConfigurationSerializable {
    static HashMap<String, AutoPig> PIGS = new HashMap<>();

    public Pig pig;
    public Player owner;
    public PigInventory inv;
    public Feature state = new FeatureIdle(this);
    public float food_saturation;



    AutoPig (Pig pig, Player player) {
        this.pig = pig;
        this.owner = player;
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
        if (item.isSimilar(Static.MINING))  set_state(PigState.Mine);
        // todo
//        if (item.isSimilar(Static.FISHING)) mine();
//        if (item.isSimilar(Static.DEFENCE)) mine();
//        if (item.isSimilar(Static.RIDE)) mine();
//        if (item.isSimilar(Static.INSTRUCTION)) mine();
    }

    public void set_state(PigState new_state) {
        if (state.getClass() == new_state.class_) {
            say_to_owner("already this state");
            return;
        }
        state.stop();


        try {
            state = new_state.constructor.newInstance(this);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}

        set_name();
    }

    void set_name() {
        pig.setCustomName("Auto Pig (" + owner.getName() + ") - " + state.get_name());
    }


    public boolean eat_to_work(float need) {
        if (food_saturation < need) {
            if (!inv.inv.removeItem(Static.PIG_FOOD).isEmpty()) // eat
                return false;
            food_saturation += 1;
        }
        food_saturation -= need;
        return true;

    }



    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("pig", pig.getUniqueId().toString());
        map.put("owner", owner.getUniqueId().toString());
        map.put("inv", inv.get_items().toArray());
        return map;
    }

    public static AutoPig deserialize(Map<String, Object> map) {
        Pig pig = (Pig) Bukkit.getEntity(UUID.fromString((String) map.get("pig")));
        Player owner = (Player) Bukkit.getEntity(UUID.fromString((String) map.get("owner")));
        AutoPig autopig = new AutoPig(pig, owner);
        autopig.inv.set_items((ArrayList<ItemStack>) map.get("inv"));

        return autopig;

    }

}
