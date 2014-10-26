package net.minecraft.mangrove.core.utils;

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
}
