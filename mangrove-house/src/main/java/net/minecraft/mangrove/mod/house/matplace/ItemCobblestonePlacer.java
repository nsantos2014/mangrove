package net.minecraft.mangrove.mod.house.matplace;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCobblestonePlacer extends ItemMaterialPlacer{
	private String name="item_coblestone_placer";
	
	public ItemCobblestonePlacer() {
		super(Blocks.cobblestone);
		GameRegistry.registerItem(this, name,MGHouseForge.ID );
		setUnlocalizedName(MGHouseForge.ID + "_" + name);
		setCreativeTab(CreativeTabs.tabTools);
		
    }
	
	public String getName() {
	    return name;
    }
}
