package net.minecraft.mangrove.mod.thrive.robot.miner;

import net.minecraft.mangrove.mod.thrive.robot.block.AbstractBlockNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRobotMinerNode extends AbstractBlockNode{


	public BlockRobotMinerNode() {
		super("robot_miner_head");
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRobotMinerNode(world);
	}
}
