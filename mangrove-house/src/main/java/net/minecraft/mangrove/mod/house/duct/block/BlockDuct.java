package net.minecraft.mangrove.mod.house.duct.block;

import net.minecraft.mangrove.mod.house.duct.AbstractBlockDuct;
import net.minecraft.mangrove.mod.house.duct.entity.TileEntityDuct;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDuct extends AbstractBlockDuct{

    public BlockDuct() {
       super();
       setBlockName("hopperduct");
       
    }
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityDuct();
	}

	@SideOnly(Side.CLIENT)
	public String getItemIconName() {
		return "mangrove:hopperduct";
	}
	
	public static TileEntityDuct getHopperDuctTile(
			IBlockAccess par0IBlockAccess, int par1, int par2, int par3) {
		return ((TileEntityDuct) par0IBlockAccess.getTileEntity(par1,
				par2, par3));
	}
}
