package net.minecraft.mangrove.mod.warfare.surpass;

import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.warfare.MGWarfareItems;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.structure.StructureVillagePieces.WoodHut;
import net.minecraftforge.common.ChestGenHooks;

public class ItemWoodenSurpass extends AbstractItemSurpass {

	public ItemWoodenSurpass() {
		super("wooden_surpass", 10.0f, 0.25f);
	}

//	@Override
//	public WeightedRandomChestContent getChestGenBase(ChestGenHooks chest, Random rnd, WeightedRandomChestContent original) {
////		if (chest == ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST)) {
//			return new WeightedRandomChestContent(new ItemStack(MGWarfareItems.woodenSurpass), 1, 1, 5000) {
//				@Override
//				protected ItemStack[] generateChestContent(Random random, IInventory newInventory) {
//
//					return new ItemStack[] { new ItemStack(MGWarfareItems.woodenSurpass) };
//				}
//			};
////		}
////		return super.getChestGenBase(chest, rnd, original);
//	}
}
