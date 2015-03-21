package net.minecraft.mangrove.mod.thrive.autocon.harvester.farmer;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractBlockHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockHarvesterFarmer extends AbstractBlockHarvester{

	public BlockHarvesterFarmer() {
		super("harvester_farmer");
	}

	@Override
	protected AbstractTileHarvester createNewTileHarvester(World worldIn, int meta) {
		return new TileHarvesterFarmer();
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if (canPlaceBlockAt) {
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos,
					MGThriveBlocks.duct_connector,
					MGThriveBlocks.harvester_miner);
			canPlaceBlockAt=result.isEmpty();
		}
		return canPlaceBlockAt;
	}
	
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
	}
}
