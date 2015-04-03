package net.minecraft.mangrove.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.mangrove.core.MGCoreForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemHardconium extends Item{
	private String name="hardconium_rod";

	public ItemHardconium() {
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(MGCoreForge.ID + "_" + name);
		setCreativeTab(CreativeTabs.tabMisc);

	}
	public String getName() {
		return name;
	}
}
