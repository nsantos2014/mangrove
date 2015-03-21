package net.minecraft.mangrove.mod.thrive.autocon.harvester.miner;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractBlockHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.World;

public class TileHarvesterMiner extends AbstractTileHarvester {
	public static final Set<Block> drillableBlocks = new HashSet<Block>();
	private final Random field_149933_a = new Random();
	private int depth = 1;
    private int height = 0;


	@Override
	protected boolean doInit(int serverTick) {
		EnumFacing enumfacing = (EnumFacing) worldObj.getBlockState(pos).getValue(AbstractBlockHarvester.FACING);
		if (this.depth >= 10) {
			this.depth = 1;
		}
		for (int j = 1; j <= depth; j++) {
			BlockPos currentDepth = pos.offset(enumfacing.getOpposite(), j);
			int validate = validate(currentDepth);
			if(validate>=0){
				this.depth=j;
				return validate==1;
			}
		}
		
		BlockPos currentDepth = pos.offset(enumfacing.getOpposite(), depth);
		for (int j = 0; j <currentDepth.getY(); j++) {
			BlockPos currentHeight = currentDepth.down(j);
			int validate = validate(currentHeight);
			if(validate>=0){	
				this.height=j;
				return validate==1;
			}
		}

		return false;
	}
	private int validate(BlockPos currentDepth) {
		final IBlockState blockState = this.worldObj.getBlockState(currentDepth);
		final Block block =blockState.getBlock() ;
		if (block.isAir(this.worldObj, currentDepth)) {
//		    System.out.println("Block Air(" + currentDepth.getX() + "," + currentDepth.getY() + "," + currentDepth.getZ() + "): " + block);
		    // return false;
		} else if (isLiquid(block, this.worldObj, currentDepth)) {
//		    System.out.println("Block Air(" + currentDepth.getX() + "," + currentDepth.getY() + "," + currentDepth.getZ() + "): " + block);
		    // return false;
		} else {
		    if (drillableBlocks.contains(block)) {
//		        System.out.println("Block Drillable (" + currentDepth.getX() + "," + currentDepth.getY() + "," + currentDepth.getZ() + "): " + block);
		        return 1;
		    }

//		    System.out.println("Block Not Drillable (" + currentDepth.getX() + "," + currentDepth.getY() + "," + currentDepth.getZ() + "): " + block);
		    return 0;
		}
		return -1;
	}
	private boolean isLiquid(Block block, World worldObj, BlockPos blockPos) {
        return block.getMaterial().isLiquid();
    }


	@Override
	protected boolean doExecute(int serverTick) {
		EnumFacing enumfacing = (EnumFacing) worldObj.getBlockState(pos).getValue(AbstractBlockHarvester.FACING);
		
		BlockPos currentDepth = pos.offset(enumfacing.getOpposite(), depth);
		for (int j = 0; j <currentDepth.getY(); j++) {
			BlockPos currentHeight = currentDepth.down(j);
			int validate = validate(currentHeight);
			if(validate==0){
				return false;
			}
			if(validate==1){
				IBlockState blockState = this.worldObj.getBlockState(currentHeight);
		        
		        final Block block = blockState.getBlock();
				final ITransactor transactor = getTransactor();
				final List<ItemStack> drops = block.getDrops(worldObj, currentHeight, blockState, 0);
				if (block.hasTileEntity(blockState)) {
	                worldObj.removeTileEntity(currentHeight);
	            }
				worldObj.setBlockToAir(currentHeight);
				if (transactor != null) {
	                for (ItemStack iStack : drops) {
	                    ItemStack stack = transactor.add(iStack, EnumFacing.UP, true);
	                    if (stack.stackSize < iStack.stackSize) {
	                        iStack.stackSize -= stack.stackSize;
	                        spawnDrop(currentHeight, iStack);
	                    }
	                }

	                AxisAlignedBB axis = AxisAlignedBB.fromBounds(currentHeight.getX() - 2, currentHeight.getY() - 2, currentHeight.getZ() - 2, currentHeight.getX() + 3, currentHeight.getY() + 3, currentHeight.getZ() + 3);
	                List result = worldObj.getEntitiesWithinAABB(EntityItem.class, axis);
	                for (int ii = 0; ii < result.size(); ii++) {
	                    if (result.get(ii) instanceof EntityItem) {
	                        EntityItem entity = (EntityItem) result.get(ii);
	                        if (entity.isDead) {
	                            continue;
	                        }

	                        ItemStack iStack = entity.getEntityItem();
	                        if (iStack.stackSize <= 0) {
	                            continue;
	                        }
	                        entity.worldObj.removeEntity(entity);
	                        ItemStack stack = transactor.add(iStack, EnumFacing.UP, true);
	                        if (stack.stackSize < iStack.stackSize) {
	                            iStack.stackSize -= stack.stackSize;
	                            spawnDrop(currentHeight, iStack);
	                            break;
	                        }
	                    }
	                }
	            } else {
	                spawnCropsByDefault(currentHeight, drops);
	            }

	            block.breakBlock(worldObj, currentHeight, blockState);
	            this.markDirty();
				return true;
			}
		}

		return false;
	}

	private void spawnCropsByDefault(final BlockPos worldPos, final List<ItemStack> drops) {
        for (ItemStack iStack : drops) {
            spawnDrop(worldPos, iStack);
        }
    }

    private void spawnDrop(final BlockPos worldPos, ItemStack iStack) {
        if (iStack != null) {
            float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
            float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
            float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

            final EntityItem entityitem = new EntityItem(worldObj, (double) ((float) worldPos.getX() + f), (double) ((float) worldPos.getY() + 1 + f1), (double) ((float) worldPos.getZ() + f2),
                    iStack);

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) this.field_149933_a.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) this.field_149933_a.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) this.field_149933_a.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }
	
	@Override
	protected void doCommit(int serverTick) {
		this.depth++;

	}

	@Override
	protected void doRollback(int serverTick) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean renderScene(int clientTick) {
		EnumFacing enumfacing = (EnumFacing) worldObj.getBlockState(pos).getValue(AbstractBlockHarvester.FACING);
		BlockPos position = pos.offset(enumfacing.getOpposite(), depth).down(this.height-1);
		AxisDirection dir = enumfacing.getOpposite().getAxisDirection();

		worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, offsetX(position, dir, 0.25), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.25), 0.5D*dir.getOffset(), 1.5D, 0.5D*dir.getOffset());
		worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, offsetX(position, dir, 0.25), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.75), 0.5D*dir.getOffset(), 1.5D, 0.5D*dir.getOffset());
		worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, offsetX(position, dir, 0.50), offsetY(position, dir, 0.1), offsetZ(position, dir, 0.50), 0.0D, 0.5D, 0.0D);
		worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, offsetX(position, dir, 0.75), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.25), 0.5D*dir.getOffset(), 1.5D, 0.5D*dir.getOffset());
		worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, offsetX(position, dir, 0.75), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.75), 0.5D*dir.getOffset(), 1.5D, 0.5D*dir.getOffset());

		return true;
	}

	@Override
	protected boolean renderSceneOut(int clientTick) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean renderCooldown(int clientTick) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean renderFailure(int clientTick) {
		Random rand = new Random();
		BlockPos uppPos = this.pos.offset(EnumFacing.UP);
		double d0 = (double) uppPos.getX() + 0.5D;
		double d1 = (double) uppPos.getY() + rand.nextDouble() * 6.0D / 16.0D;
		double d2 = (double) uppPos.getZ() + 0.5D;
		double d3 = 0.52D;
		double d4 = rand.nextDouble() * 0.6D - 0.3D;

		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		depth = tag.getInteger("depth");
		height = tag.getInteger("height");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("depth", depth);
		tag.setInteger("height", height);
	}

	@Override
	public JsonObject getTilePacketData() {
		JsonObject tilePacketData = super.getTilePacketData();
		tilePacketData.addProperty("depth", depth);
		tilePacketData.addProperty("height", height);		
		return tilePacketData;
	}

	@Override
	protected void handleServerEvent(JsonObject data) {
		if (data.has("depth")) {
			this.depth = data.get("depth").getAsInt();
		}
		if (data.has("height")) {
			this.height = data.get("height").getAsInt();
		}
	}

	@Override
	protected void handleClientEvent(JsonObject data) {
		if (data.has("depth")) {
			this.depth = data.get("depth").getAsInt();
		}
		if (data.has("height")) {
			this.height = data.get("height").getAsInt();
		}
	}

	static {
		drillableBlocks.clear();
		drillableBlocks.add(Blocks.grass);
		drillableBlocks.add(Blocks.farmland);
		drillableBlocks.add(Blocks.mycelium);
		drillableBlocks.add(Blocks.obsidian);
		// drillableBlocks.add(Blocks.snow);
		drillableBlocks.add(Blocks.dirt);
		drillableBlocks.add(Blocks.sand);		
		drillableBlocks.add(Blocks.gravel);
		drillableBlocks.add(Blocks.sandstone);
		drillableBlocks.add(Blocks.red_sandstone);
		drillableBlocks.add(Blocks.stone);
		drillableBlocks.add(Blocks.clay);
		drillableBlocks.add(Blocks.hardened_clay);
		drillableBlocks.add(Blocks.stained_hardened_clay);

		drillableBlocks.add(Blocks.coal_ore);
		drillableBlocks.add(Blocks.iron_ore);
		drillableBlocks.add(Blocks.gold_ore);
		drillableBlocks.add(Blocks.emerald_ore);
		drillableBlocks.add(Blocks.diamond_ore);
		drillableBlocks.add(Blocks.lapis_ore);
		drillableBlocks.add(Blocks.emerald_ore);
		drillableBlocks.add(Blocks.redstone_ore);
		drillableBlocks.add(Blocks.lit_redstone_ore);
		
		drillableBlocks.add(Blocks.prismarine);
		drillableBlocks.add(Blocks.quartz_ore);
				
	}
}
