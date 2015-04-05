package net.minecraft.mangrove.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.items.ItemHardconium;
import net.minecraft.mangrove.core.recipes.RecipeBuilder;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGCoreItems {
	public static final ArmorMaterial HARDCONIUM = EnumHelper.addArmorMaterial("HARDCONIUM", "hardconium", 44, new int[] { 3, 8, 6, 3 }, 50);
	public static ItemHardconium hardconium_rod;

	public static void preInit() {
		hardconium_rod=new ItemHardconium();
	}
	
	 public static void registerItems(FMLInitializationEvent event) {
		 if (event.getSide() == Side.CLIENT) {
	            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	            ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
	            itemModelMesher.register(hardconium_rod, 0, new ModelResourceLocation(MGCoreForge.ID + ":" +hardconium_rod.getName(), "inventory"));
	        }
		 
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(hardconium_rod,4).line(null, Blocks.yellow_flower, null).line(null, Blocks.yellow_flower, null).line(Items.iron_ingot, Items.iron_ingot, Items.iron_ingot).build());
		
		ItemStack blueDye=new ItemStack(Items.dye, 2, EnumDyeColor.BLUE.getDyeDamage());
		ItemStack lightBlueDye=new ItemStack(Items.dye,1, EnumDyeColor.LIGHT_BLUE.getDyeDamage());
		ItemStack blackDye=new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage());
		GameRegistry.addRecipe(
				new ShapedOreRecipe(blueDye,
				"LB",
				'L',lightBlueDye,
				'B',blackDye
				));
			
	 }

}
