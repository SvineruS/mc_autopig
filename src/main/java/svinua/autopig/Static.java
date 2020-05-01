package svinua.autopig;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Static {
    public static final ItemStack PIG_FOOD = new ItemStack(Material.CARROT, 1);

    final static ItemStack INSTRUCTION = item(Material.BOOK, "ИНСТРУКЦИЯ",
            "Тут будет инструкция"
    );

    final static ItemStack MINING = item(Material.IRON_PICKAXE, "Ебать копать",
            "1) Дай мне кирку\n" +
                    "2) Выдели область WorldEdit-ом\n" +
                    "3) Нажми на кнопку"
    );

    final static ItemStack DEFENCE = item(Material.SHIELD, "Убивать мобов в радиусе",
            "1) Дай мне меч\n" +
                    "2) Нажми на кнопку"
    );

    final static ItemStack FISHING = item(Material.FISHING_ROD, "Ловить рыбу",
            "1) Отведи меня к воде\n" +
                    "2) Нажми на кнопку"
    );

    final static ItemStack RIDE = item(Material.SADDLE, "Взять на руки", "На самом деле на голову.\n" +
            "Нажми еще раз что бы снять");



    final static ItemStack AUTOPIG_CREATOR = new ItemStack(Material.GOLDEN_CARROT, 1);

    
    public static ItemStack[] PIG_MENU = new ItemStack[] {INSTRUCTION, MINING, DEFENCE, FISHING, RIDE};


    static ItemStack item(Material material, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.split("\n")));
        item.setItemMeta(meta);
        return item;
    }
}
