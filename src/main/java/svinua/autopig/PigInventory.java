package svinua.autopig;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PigInventory implements InventoryHolder {

    public Inventory inv = Bukkit.createInventory(this, 27, "Auto Pig");

    public PigInventory() {
        put_menu_in_inventory();
    }


    public void set_items(ArrayList<ItemStack>  inv_items) {
        for (int i=0; i<18; i++)
            inv.setItem(i, inv_items.get(i));

    }

    public ArrayList<ItemStack> get_items() {
        ArrayList<ItemStack> inv_items = new ArrayList<>();
        for (int i=0; i<18; i++)
            inv_items.add(this.inv.getItem(i));
        return inv_items;
    }




    public ItemStack find_pickaxe() {
        for (ItemStack item: get_items()) {
            if (item == null) continue;
            if (item.getType().toString().toLowerCase().contains("pickaxe")) return item;
        }
        return null;
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

}
