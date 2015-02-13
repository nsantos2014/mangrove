package net.minecraft.mangrove.mod.warfare;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.warfare.armor.ItemXArmor;
import net.minecraft.mangrove.mod.warfare.surpass.AbstractItemSurpass;
import net.minecraft.mangrove.mod.warfare.surpass.ItemDiamondSurpass;
import net.minecraft.mangrove.mod.warfare.surpass.ItemIronSurpass;
import net.minecraft.mangrove.mod.warfare.surpass.ItemWoodenSurpass;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGWarfareItems {
    //public static final ArmorMaterial ATER = EnumHelper.addArmorMaterial("ATER", "chainmail", 44, new int[] { 3, 8, 6, 3 }, 50);

    public static AbstractItemSurpass woodenSurpass;
    public static AbstractItemSurpass ironSurpass;
    public static AbstractItemSurpass diamondSurpass;
    public static ItemXArmor x_helmet;
    public static ItemXArmor x_chestplate;
    public static ItemXArmor x_leggings;
    public static ItemXArmor x_boots;

    // name, harvestLevel, durability, miningSpeed, damageVsEntities,
    // enchantability

    public static void preInit() {
        woodenSurpass = new ItemWoodenSurpass();
        ironSurpass = new ItemIronSurpass();
        diamondSurpass = new ItemDiamondSurpass();

        x_helmet = new ItemXArmor(ArmorMaterial.CHAIN, "diamond_helmet", 1, 0);
        x_chestplate = new ItemXArmor(ArmorMaterial.CHAIN, "diamond_chestplate", 1, 1);
        x_leggings = new ItemXArmor(ArmorMaterial.CHAIN, "diamond_leggings", 1, 2);
        x_boots = new ItemXArmor(ArmorMaterial.CHAIN, "diamond_boots", 1, 3);
    }

    public static void registerArmor(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
            itemModelMesher.register(MGWarfareItems.x_helmet, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "diamond_helmet", "inventory"));
            itemModelMesher.register(MGWarfareItems.x_chestplate, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "diamond_chestplate", "inventory"));
            itemModelMesher.register(MGWarfareItems.x_leggings, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "diamond_leggings", "inventory"));
            itemModelMesher.register(MGWarfareItems.x_boots, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "diamond_boots", "inventory"));
        }
    }

    public static void registerSurpass(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
            itemModelMesher.register(MGWarfareItems.woodenSurpass, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "wooden_surpass", "inventory"));
            itemModelMesher.register(MGWarfareItems.ironSurpass, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "iron_surpass", "inventory"));
            itemModelMesher.register(MGWarfareItems.diamondSurpass, 0, new ModelResourceLocation(MGWarfareForge.ID + ":" + "diamond_surpass", "inventory"));
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.woodenSurpass, 1), new Object[] { Boolean.valueOf(true), "PwA", "wsw", "SwW", Character.valueOf('P'), Items.stone_pickaxe, Character.valueOf('A'), Items.stone_axe, Character.valueOf('S'),
                Items.stone_shovel, Character.valueOf('W'), Items.stone_sword, Character.valueOf('s'), Blocks.stone, Character.valueOf('w'), Blocks.planks }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.ironSurpass, 1), new Object[] { Boolean.valueOf(true), "PiA", "isi", "SiW", Character.valueOf('P'), Items.iron_pickaxe, Character.valueOf('A'), Items.iron_axe, Character.valueOf('S'),
                Items.iron_shovel, Character.valueOf('W'), Items.iron_sword,

                Character.valueOf('i'), new ItemStack(Items.iron_ingot, 4), Character.valueOf('s'), new ItemStack(MGWarfareItems.woodenSurpass, 1) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.diamondSurpass, 1), new Object[] { Boolean.valueOf(true), "PdA", "dsd", "SdW", Character.valueOf('P'), Items.diamond_pickaxe, Character.valueOf('A'), Items.diamond_axe, Character.valueOf('S'),
                Items.diamond_shovel, Character.valueOf('W'), Items.diamond_sword,

                Character.valueOf('d'), Items.diamond, Character.valueOf('s'), new ItemStack(MGWarfareItems.ironSurpass, 1) }));
    }
}
