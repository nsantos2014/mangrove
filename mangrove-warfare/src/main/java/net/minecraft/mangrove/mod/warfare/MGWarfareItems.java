package net.minecraft.mangrove.mod.warfare;

import net.minecraft.item.Item;
import net.minecraft.mangrove.mod.warfare.rifle.ItemBlasterRifle;
import net.minecraft.mangrove.mod.warfare.rifle.ItemXArmor;

public class MGWarfareItems {
    public static final Item blasterRifle = new ItemBlasterRifle();
    public static final ItemXArmor x_helmet = (ItemXArmor)(new ItemXArmor( 1, 0)).setUnlocalizedName("helmetX").setTextureName("chainmail_helmet");
    public static final ItemXArmor x_chestplate = (ItemXArmor)(new ItemXArmor(1, 1)).setUnlocalizedName("chestplateX").setTextureName("chainmail_chestplate");
    public static final ItemXArmor x_leggings = (ItemXArmor)(new ItemXArmor(1, 2)).setUnlocalizedName("leggingsX").setTextureName("chainmail_leggings");
    public static final ItemXArmor x_boots = (ItemXArmor)(new ItemXArmor(1, 3)).setUnlocalizedName("bootsX").setTextureName("chainmail_boots");
    
}
