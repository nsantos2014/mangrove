package net.minecraft.mangrove.core.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUtils {
	public static int getDirectionFromMetadata(int meta) {
		return (meta & 0x7);
	}
	
	public static ForgeDirection getForgeDirectionFromMetadata(int meta) {
		return ForgeDirection.getOrientation(meta & 0x7);
	}
	
	public static boolean getIsBlockNotPoweredFromMetadata(int meta) {
		return ((meta & 0x8) != 8);
	}
	
	public static List<ItemStack> getItemStackFromBlock(WorldServer world, int i, int j, int k) {
        Block block = world.getBlock(i, j, k);

        if (block == null || block.isAir(world, i, j, k)) {
            return null;
        }

        int meta = world.getBlockMetadata(i, j, k);

        ArrayList<ItemStack> dropsList = block.getDrops(world, i, j, k, meta, 0);
        float dropChance = 0;
//        float dropChance = ForgeEventFactory.fireBlockHarvesting(dropsList, world, block, i, j, k, meta, 0, 1.0F,
//                false, CoreProxy.proxy.getBuildCraftPlayer(world).get());

        ArrayList<ItemStack> returnList = new ArrayList<ItemStack>();
        for (ItemStack s : dropsList) {
            if (world.rand.nextFloat() <= dropChance) {
                returnList.add(s);
            }
        }

        return returnList;
    }
}
