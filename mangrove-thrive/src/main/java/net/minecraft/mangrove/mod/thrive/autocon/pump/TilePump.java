package net.minecraft.mangrove.mod.thrive.autocon.pump;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.BlockAutobench;
import net.minecraft.mangrove.mod.thrive.cistern.TileCistern;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.BlockFluidBase;

public class TilePump extends AbstractTileAutocon implements IUpdatePlayerListBox {
	private long tick = 0;

	private Deque<BlockPos> pumpLayerQueue =new ArrayDeque<BlockPos>();
	
	public TilePump() {
	}

	@Override
	public void update() {
		if (!isPowered()) {
			tick=0;
			return;
		}
		tick++;
		PumpProcess process = new PumpProcess();
		if ((this.worldObj == null) || (this.worldObj.isRemote)) {
			if (tick % 8 == 0 && !process.validate()) {
				renderFailure();
			}
			return;
		}
		
		if (tick % 128 == 0) {
			if( !process.validate()){
				return;
			}
			process.doAction();
		}	
	}

	public boolean isPowered() {
		return (Boolean) worldObj.getBlockState(pos).getValue(BlockAutobench.POWERED);
	}

	private void renderFailure() {
		Random rand = new Random();
		BlockPos uppPos = this.pos.offset(EnumFacing.UP);
		double d0 = (double) uppPos.getX() + 0.5D;
		double d1 = (double) uppPos.getY() + rand.nextDouble() * 6.0D / 16.0D;
		double d2 = (double) uppPos.getZ() + 0.5D;
		double d3 = 0.52D;
		double d4 = rand.nextDouble() * 0.6D - 0.3D;

		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
		
	}

	private class PumpProcess{
		private Material liquid;
		private BlockPos liquidPos;
		private boolean valid;
		private TileCistern useCistern;
		private BlockPos cisternPos;
		ItemBucket bucket=null;
		
		public PumpProcess() {
			// TODO Auto-generated constructor stub
		}		
		public boolean validate(){			
			findNearLiquid();
			if( !valid){
				return false;
			}
			findNearCisterns();
			return valid;
		}
		
		private void findNearLiquid(){
			valid=false;
			liquid=null;
			liquidPos=null;
			BlockPos probePos;
			for(int i=1; i<=6; i++){
				probePos=pos.down(i);
				IBlockState blockState = worldObj.getBlockState(probePos);
				Block block = blockState.getBlock();
				Material material = block.getMaterial();
				if(material.isLiquid()){
					liquid=material;
					liquidPos=probePos;
					valid=true;
					return;
				}else if( material!=Material.air) {
					return;
				}
			}
		}
		
		private void findNearCisterns() {
			valid=false;
			useCistern=null;
			bucket=null;
			IBlockState tileBlockState = worldObj.getBlockState(pos);
			EnumFacing facing = (EnumFacing) tileBlockState.getValue(BlockPump.FACING);
			EnumSet<EnumFacing> set = EnumSet.of(facing.getOpposite(), facing.rotateY(), facing.rotateYCCW());
			BlockPos probePos;
			for( EnumFacing testFacing:set){
				probePos=pos.offset(testFacing);
				IBlockState blockState = worldObj.getBlockState(probePos);
				Block block = blockState.getBlock();
				if( block==MGThriveBlocks.cistern) {
					TileCistern cistern=(TileCistern) worldObj.getTileEntity(probePos);
					
					if( liquid==Material.water){
						bucket=(ItemBucket) Items.water_bucket;
					} else if( liquid==Material.lava){
						bucket=(ItemBucket) Items.lava_bucket;
					}
					if( bucket!=null){
						if( cistern.canPlace(bucket) ){
							useCistern=cistern;
							cisternPos=probePos;
							valid=true;		
							return;
						}
					}					
				}
			}
		}
		public void doAction() {
			if(!valid || useCistern==null || 	liquid==null||liquidPos==null){
				return;
			}
			rebuildQueue();
			while(poolLiquid()){}
			
		}
		private boolean poolLiquid() {
			BlockPos liqPos = pumpLayerQueue.pollLast();
			if( liqPos!=null){
				int meta = ((Integer) worldObj.getBlockState(liqPos).getValue(BlockFluidBase.LEVEL)).intValue();

				if (meta != 0) {
					return true;
				}

				
				worldObj.setBlockToAir(liqPos);
				useCistern.place(bucket);
			}
			return false;
		}
		private void rebuildQueue() {
			pumpLayerQueue.clear();
			
			Set<BlockPos> visitedBlocks = new HashSet<BlockPos>();
			Deque<BlockPos> fluidsFound = new LinkedList<BlockPos>();
			
			queueForPumping(liquidPos, visitedBlocks, fluidsFound);
//			System.out.println("Fluids Found : "+fluidsFound);
			while (!fluidsFound.isEmpty()) {
				Deque<BlockPos> fluidsToExpand = fluidsFound;
				fluidsFound = new LinkedList<BlockPos>();

				for (BlockPos index : fluidsToExpand) {
//					queueForPumping(index.down(), visitedBlocks, fluidsFound);
					queueForPumping(index.east(), visitedBlocks, fluidsFound);
					queueForPumping(index.west(), visitedBlocks, fluidsFound);
					queueForPumping(index.north(), visitedBlocks, fluidsFound);
					queueForPumping(index.south(), visitedBlocks, fluidsFound);
				}
				
				
			}
//			System.out.println("Fluids Found : "+pumpLayerQueue);
		}
		private void queueForPumping(BlockPos index, Set<BlockPos> visitedBlocks, Deque<BlockPos> fluidsFound) {
			if (visitedBlocks.add(index)) {
				if ((index.getX() - pos.getX()) * (index.getX() - pos.getY()) + index.getZ() - pos.getZ() * (index.getZ() - pos.getZ()) > 64 * 64) {
					return;
				}

				Block block = worldObj.getBlockState(index).getBlock();

				if (block.getMaterial() == liquid) {
					fluidsFound.add(index);
				}
				if (block.getMaterial() == liquid && block instanceof BlockStaticLiquid) { // TODO Check if can drain
					pumpLayerQueue.add(index);
				}
			}
		}
		
		
	}

}
