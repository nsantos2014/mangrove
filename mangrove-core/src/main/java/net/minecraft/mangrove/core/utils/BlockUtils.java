package net.minecraft.mangrove.core.utils;

public class BlockUtils {
	public static int getDirectionFromMetadata(int meta) {
		return (meta & 0x7);
	}
	
	public static boolean getIsBlockNotPoweredFromMetadata(int meta) {
		return ((meta & 0x8) != 8);
	}
}
