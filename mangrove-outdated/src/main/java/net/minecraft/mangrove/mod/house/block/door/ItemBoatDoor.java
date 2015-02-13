package net.minecraft.mangrove.mod.house.block.door;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.house.MGHouseBlocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBoatDoor extends Item {
	private Material doorMaterial;

	public ItemBoatDoor() {
		this.doorMaterial = Material.rock;
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setUnlocalizedName("boat_door_item");
		this.setTextureName("mangrove:boat_door");
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player,
			World world, int x, int y, int z, int par7, float par8, float par9,
			float par10) {
		if (par7 != 0) {
			return false;
		} else {
			--y;
			Block block;

			// if (this.doorMaterial == Material.wood)
			// {
			// block = Blocks.wooden_door;
			// }
			// else
			// {
			// block = Blocks.iron_door;
			// }
			block = MGHouseBlocks.boat_door;

			if (player.canPlayerEdit(x, y, z, par7, itemStack)
					&& player.canPlayerEdit(x, y - 1, z, par7, itemStack)) {
				if (!block.canPlaceBlockAt(world, x, y + 1, z)) {
					return false;
				} else {
					int i1 = MathHelper
							.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					placeDoorBlock(world, x, y - 1, z, i1, block);
					--itemStack.stackSize;
					return true;
				}
			} else {
				return false;
			}
		}
	}

	public static void placeDoorBlock(World world, int x, int y, int z,
			int p_150924_4_, Block block) {
		byte b0 = 0;
		byte b1 = 0;

		if (p_150924_4_ == 0) {
			b1 = 1;
		}

		if (p_150924_4_ == 1) {
			b0 = -1;
		}

		if (p_150924_4_ == 2) {
			b1 = -1;
		}

		if (p_150924_4_ == 3) {
			b0 = 1;
		}

		int i1 = (world.getBlock(x - b0, y, z - b1).isNormalCube() ? 1 : 0)
				+ (world.getBlock(x - b0, y + 1, z - b1).isNormalCube() ? 1 : 0);
		int j1 = (world.getBlock(x + b0, y, z + b1).isNormalCube() ? 1 : 0)
				+ (world.getBlock(x + b0, y + 1, z + b1).isNormalCube() ? 1 : 0);
		boolean flag = world.getBlock(x - b0, y, z - b1) == block
				|| world.getBlock(x - b0, y + 1, z - b1) == block;
		boolean flag1 = world.getBlock(x + b0, y, z + b1) == block
				|| world.getBlock(x + b0, y + 1, z + b1) == block;
		boolean flag2 = false;

		if (flag && !flag1) {
			flag2 = true;
		} else if (j1 > i1) {
			flag2 = true;
		}

		world.setBlock(x, y, z, block, p_150924_4_, 2);
		world.setBlock(x, y + 1, z, block, 8 | (flag2 ? 1 : 0), 2);
		world.notifyBlocksOfNeighborChange(x, y, z, block);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
	}
}
