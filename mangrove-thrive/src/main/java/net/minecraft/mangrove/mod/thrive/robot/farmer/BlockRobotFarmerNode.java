package net.minecraft.mangrove.mod.thrive.robot.farmer;

import net.minecraft.mangrove.mod.thrive.robot.block.AbstractBlockNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRobotFarmerNode extends AbstractBlockNode{


	public BlockRobotFarmerNode() {
		super("robot_farmer_head");
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRobotFarmerNode();
	}
}
