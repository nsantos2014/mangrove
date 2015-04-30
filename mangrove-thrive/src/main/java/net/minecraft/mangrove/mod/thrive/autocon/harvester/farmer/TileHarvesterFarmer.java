package net.minecraft.mangrove.mod.thrive.autocon.harvester.farmer;

import java.util.List;
import java.util.Random;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.filter.ArrayStackFilter;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractBlockHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.junction.BlockStorageJunction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class TileHarvesterFarmer extends AbstractTileHarvester {
	private final Random field_149933_a = new Random();
	public int depth = 1;
//	public int height = 0;
	private ItemStack seedStack;

	public TileHarvesterFarmer() {
		this.seedStack = new ItemStack(Items.wheat_seeds, 1);
	}

	@Override
	protected boolean doInit(int serverTick) {
		EnumFacing enumfacing = (EnumFacing) worldObj.getBlockState(pos).getValue(AbstractBlockHarvester.FACING);
		if (this.depth >= 10) {
			this.depth = 1;
		}
		for (int j = 1; j <= depth; j++) {
			BlockPos currentDepth = pos.offset(enumfacing.getOpposite(), 1 + j);
			for (int i = 0; i < 3; i++) {
				BlockPos current = currentDepth.down(i);
				if (!worldObj.isAirBlock(current)) {
					IBlockState blockState = worldObj.getBlockState(current);
					Block block = blockState.getBlock();
					if (!(block instanceof IGrowable) ) {
						depth = j;
//						System.out.println("Failed : 1 :"+depth+": "+block);
						return false;
					}
				}
			}

			BlockPos current = currentDepth.down(3);
			IBlockState blockState = worldObj.getBlockState(current);
			Block block = blockState.getBlock();
			if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.farmland) {
				depth = j;
//				System.out.println("Failed : 2 :"+depth+": "+block);
				return false;
			}
		}

		return true;
	}
	private boolean isWater(Block block, World worldObj, BlockPos blockPos) {
        return block.getMaterial() == Material.water;
    }

	@Override
	protected boolean doExecute(int serverTick) {
//		System.out.println("Execute");
		EnumFacing enumfacing = (EnumFacing) worldObj.getBlockState(pos).getValue(AbstractBlockHarvester.FACING);
		BlockPos currentDepth = pos.offset(enumfacing.getOpposite(), depth);
		for (int i = 0; i < 4; i++) {
			BlockPos current = currentDepth.down(i);
			if (!worldObj.isAirBlock(current)) {
				IBlockState blockState = worldObj.getBlockState(current);
				Block block = blockState.getBlock();
				if (block == Blocks.dirt || block == Blocks.grass) {
					this.worldObj.setBlockState(current, Blocks.farmland.getDefaultState(), 2);
					this.markDirty();
					break;
				}
				if (block == Blocks.farmland) {
					final ITransactor transactor = getTransactor();
					this.worldObj.setBlockState(current, blockState.withProperty(BlockFarmland.MOISTURE, 7), 2);
					BlockPos upPos = current.up();

					if (worldObj.isAirBlock(upPos)) {
						final ItemStack iStack = transactor.remove(new ArrayStackFilter(seedStack), EnumFacing.UP, false);
						if (iStack != null && iStack.stackSize > 0) {
							transactor.remove(new ArrayStackFilter(seedStack), EnumFacing.UP, true);
							this.worldObj.setBlockState(upPos, convertToBlock(seedStack).getDefaultState(), 3);
						}
					}
					IBlockState upState = worldObj.getBlockState(upPos);
					Block upBlock = upState.getBlock();
					if (upBlock instanceof IGrowable) {
						int value = (Integer) upState.getValue(BlockCrops.AGE);
						if (value == 7) {
							final List<ItemStack> drops = upBlock.getDrops(worldObj, upPos, upState, 0);

							// worldObj.setBlockToAir(upPos);
							if (transactor != null) {
								ItemStack iStack = getSeedFromDrops(drops);
								if (iStack == null) {
									iStack = transactor.remove(new ArrayStackFilter(seedStack), EnumFacing.UP, false);
									if (iStack != null && iStack.stackSize > 0) {
										transactor.remove(new ArrayStackFilter(seedStack), EnumFacing.UP, true);
									}
								}
								if (iStack != null && iStack.stackSize > 0) {
									this.worldObj.setBlockState(upPos, convertToBlock(seedStack).getDefaultState(), 3);
								} else {
									worldObj.setBlockToAir(upPos);
								}

								addCropsToTransactor(transactor, upPos, drops);
							} else {
								spawnCropsByDefault(upPos, drops);
							}
							this.worldObj.setBlockState(upPos, upState.withProperty(BlockCrops.AGE, 0), 3);
						}
						final ItemStack iStack = transactor.remove(new ArrayStackFilter(new ItemStack(Items.dye, 1, 15)), EnumFacing.UP, false);
						if (iStack != null && iStack.stackSize > 0) {
							transactor.remove(new ArrayStackFilter(new ItemStack(Items.dye, 1, 15)), EnumFacing.UP, true);
							ItemDye.applyBonemeal(iStack, worldObj, upPos, Minecraft.getMinecraft().thePlayer);
						}
					}
					this.markDirty();
					break;
				}
			}
		}

		return true;
	}

	private Block convertToBlock(ItemStack seedStack) {
		if( seedStack.getItem()==Items.wheat_seeds){
			return Blocks.wheat;
		}
		return Blocks.carrots;
	}

	private ItemStack getSeedFromDrops(List<ItemStack> drops) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getTilePacketData() {
		JsonObject tilePacketData = super.getTilePacketData();
		tilePacketData.addProperty("depth", depth);
		return tilePacketData;
	}

	@Override
	protected void handleClientEvent(JsonObject data) {
		super.handleClientEvent(data);		
	}
	
	@Override
	protected void handleServerEvent(JsonObject data) {
		super.handleServerEvent(data);
		if (data.has("depth")) {
			this.depth = data.get("depth").getAsInt();
		}
	}

	private void addCropsToTransactor(final ITransactor transactor, BlockPos upPos, final List<ItemStack> drops) {
		for (ItemStack iStack : drops) {
			ItemStack stack = transactor.add(iStack, EnumFacing.UP, true);
			if (stack.stackSize < iStack.stackSize) {
				iStack.stackSize -= stack.stackSize;
				spawnDrop(upPos, iStack);
			}
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
//		System.out.println("RenderScene");
		EnumFacing enumfacing = (EnumFacing) worldObj.getBlockState(pos).getValue(AbstractBlockHarvester.FACING);
		BlockPos position = pos.offset(enumfacing.getOpposite(), depth).offset(EnumFacing.DOWN, 2);
		AxisDirection dir = enumfacing.getAxisDirection();

		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetX(position, dir, 0.25), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.25), 0.0D, 0.5D, 0.0D);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetX(position, dir, 0.25), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.75), 0.0D, 0.5D, 0.0D);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetX(position, dir, 0.50), offsetY(position, dir, 0.1), offsetZ(position, dir, 0.50), 0.0D, 0.5D, 0.0D);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetX(position, dir, 0.75), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.25), 0.0D, 0.5D, 0.0D);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, offsetX(position, dir, 0.75), offsetY(position, dir, 0.5), offsetZ(position, dir, 0.75), 0.0D, 0.5D, 0.0D);

		return true;
	}

	@Override
	protected boolean renderSceneOut(int clientTick) {
//		System.out.println("RenderSceneOut");
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
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.depth = compound.getInteger("depth");
//		this.height = compound.getInteger("height");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger("depth", depth);
//		compound.setInteger("height", height);
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

			final EntityItem entityitem = new EntityItem(worldObj, (double) ((float) worldPos.getX() + f), (double) ((float) worldPos.getY() + 1 + f1),
					(double) ((float) worldPos.getZ() + f2), iStack);

			float f3 = 0.05F;
			entityitem.motionX = (double) ((float) this.field_149933_a.nextGaussian() * f3);
			entityitem.motionY = (double) ((float) this.field_149933_a.nextGaussian() * f3 + 0.2F);
			entityitem.motionZ = (double) ((float) this.field_149933_a.nextGaussian() * f3);
			worldObj.spawnEntityInWorld(entityitem);
		}
	}
	
	

}
