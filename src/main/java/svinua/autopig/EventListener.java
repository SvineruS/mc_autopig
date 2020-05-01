package svinua.autopig;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import svinua.autopig.Feature.Feature;
import svinua.autopig.Feature.FeatureFarmer;

public class EventListener implements Listener {

    @EventHandler
    public final void interact(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;  // only right hand
        if (!(e.getRightClicked() instanceof Pig)) return;

        Pig pig = (Pig) e.getRightClicked();

        if (!AutoPig.is_autopig(pig)) {
            if (player.getInventory().getItemInMainHand().isSimilar(Static.AUTOPIG_CREATOR)) {
                player.getInventory().removeItem(Static.AUTOPIG_CREATOR);
                AutoPig.create(pig, player);
            }
            return;
        }

        AutoPig autoPig = AutoPig.get_autopig(pig);
        player.openInventory(autoPig.inv.inv);
    }

    @EventHandler
    public final void inventory_click(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof AutoPig)) return;
        if (e.getCurrentItem() == null) return;

        for (ItemStack item: Static.PIG_MENU) {
            if (e.getCurrentItem().isSimilar(item)) {
                e.setCancelled(true);
                ((AutoPig)holder).menu_click(item);
                return;
            }
        }
    }

    @EventHandler
    public final void inventory_click(BlockGrowEvent e) {
        BlockState block = e.getNewState();
        for (AutoPig pig: AutoPig.PIGS.values()) {
            if (pig.state instanceof FeatureFarmer && block.getLocation().distance(pig.pig.getLocation()) < 50) {
                // tood
            }
        }
    }

}
