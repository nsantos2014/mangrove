package net.minecraft.mangrove.mod.warfare;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.mangrove.mod.warfare.rifle.ItemBlasterRifle;
import net.minecraft.mangrove.mod.warfare.rifle.ItemSurpass;
import net.minecraft.mangrove.mod.warfare.rifle.ItemXArmor;
import net.minecraftforge.common.util.EnumHelper;

public class MGWarfareItems {
    public static final ArmorMaterial ATER = EnumHelper.addArmorMaterial("ATER", 44, new int[]{3, 8, 6, 3}, 50);
    
    public static final Item blasterRifle = new ItemBlasterRifle();
    public static final Item surpass = new ItemSurpass();
    public static final ItemXArmor x_helmet = (ItemXArmor)(new ItemXArmor(ATER, 1, 0)).setUnlocalizedName("helmetX").setTextureName("chainmail_helmet");
    public static final ItemXArmor x_chestplate = (ItemXArmor)(new ItemXArmor(ATER,1, 1)).setUnlocalizedName("chestplateX").setTextureName("chainmail_chestplate");
    public static final ItemXArmor x_leggings = (ItemXArmor)(new ItemXArmor(ATER,1, 2)).setUnlocalizedName("leggingsX").setTextureName("chainmail_leggings");
    public static final ItemXArmor x_boots = (ItemXArmor)(new ItemXArmor(ATER,1, 3)).setUnlocalizedName("bootsX").setTextureName("chainmail_boots");

    //name, harvestLevel, durability, miningSpeed, damageVsEntities, enchantability
    
}
