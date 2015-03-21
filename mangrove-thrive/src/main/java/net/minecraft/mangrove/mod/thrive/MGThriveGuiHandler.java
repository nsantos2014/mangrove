package net.minecraft.mangrove.mod.thrive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.mangrove.core.GUIHandler;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.ContainerAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.GuiAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.TileEntityAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.ContainerItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.GuiItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MGThriveGuiHandler extends GUIHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));// getBlockTileEntity
		if( tile instanceof TileEntityAutobench){
			return new ContainerAutobench(player.inventory, world, new BlockPos(x, y, z));
		}
		if( tile instanceof TileItemBroker){
			return new ContainerItemBroker((TileItemBroker) tile,player.inventory, player);
		}
		return super.getServerGuiElement(ID, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));// getBlockTileEntity
		if( tile instanceof TileEntityAutobench){
			return new GuiAutobench(player.inventory, world,new BlockPos(x, y, z));
		}
		if( tile instanceof TileItemBroker){
			return new GuiItemBroker((TileItemBroker) tile,player.inventory);
		}
		return super.getClientGuiElement(ID, player, world, x, y, z);
	}

}
