package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.core.utils.BlockUtils;

public class TileFarmerNode extends AbstractTileRobotNode {
	private final Random field_149933_a = new Random();
	protected CSPosition3i position;
	protected CSPosition3i local;
	protected int it=0;
	protected CS localCS;
	
	public TileFarmerNode() {
		super();
		setMaxStep(10);
	}
	
	@Override
	protected boolean doInit(int serverTick) {
		this.localCS=null;
		this.local=null;
		incStep();		
		setupCS();
		System.out.println("doInit:"+serverTick);
		System.out.println("Block Base at ("+
				this.position.x+","+ 
				this.position.y+","+
				this.position.z+") = ");
		for(;this.local.y>-4;this.local.y--){
			final CSPosition3i worldPos = localCS.toWorld(local);
			final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
			if( this.local.y>-2 && !block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z)){
				System.out.println("Block Error #1 at ("+
						this.local.x+","+ 
						this.local.y+","+
						this.local.z+") ="+block);
				this.local.y=0;				
				return false;
			} else if( this.local.y==-2 && !(block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z) || (block instanceof IGrowable))){
				
				System.out.println("Block Error #2 at ("+
						this.local.x+","+ 
						this.local.y+","+
						this.local.z+") ="+block);
				this.local.y=0;				
				return false;
			} else if(this.local.y==-3 && block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z) ){
				System.out.println("Block Error #3 at ("+
						this.local.x+","+ 
						this.local.y+","+
						this.local.z+") ="+block);
				this.local.y=0;
				return false;							
			}			
		}
		this.local.y=-3;
		return true;
	}

	private void setupCS() {
		if(this.localCS==null){
			this.position=new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
			this.localCS = CS.subSystem(this.position);				
			////////////////////////////////////		
			this.position = localCS.toWorld(new CSPosition3i(getStep(),0,0,position.direction));
			this.localCS = CS.subSystem(this.position);
		}
		//////////////////////////////////
		if( this.local==null){
			this.local= new CSPosition3i();
			this.local.direction=position.direction;
		}
	}

	@Override
	protected boolean doExecute(int serverTick) {
		setupCS();
		final CSPosition3i worldPos = localCS.toWorld(local);
		final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
		
		System.out.println("doExecute:"+serverTick);
		System.out.println("Block      at ("+
				worldPos.x+","+ 
				worldPos.y+","+
				worldPos.z);
		final Block block1 = Blocks.farmland;
		if( block==Blocks.dirt || block==Blocks.grass){			
			this.worldObj.playSoundEffect((double)((float)(worldPos.x) + 0.5F), (double)((float)(worldPos.y) + 0.5F), (double)((float)(worldPos.z) + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
			this.worldObj.setBlock(worldPos.x, worldPos.y, worldPos.z, block1,7,2);
			this.markDirty();			
		} else if( block==Blocks.farmland){
			this.worldObj.setBlockMetadataWithNotify(worldPos.x, worldPos.y, worldPos.z, 7, 2);
			Block blockCrop = this.worldObj.getBlock(worldPos.x, worldPos.y+1, worldPos.z);
			if( blockCrop.isAir(this.worldObj,worldPos.x, worldPos.y+1, worldPos.z)){
				System.out.println("IS Ready to plant crop");
				worldObj.playSoundEffect((double)((float)worldPos.x + 0.5F), (double)((float)worldPos.y+1 + 0.5F), (double)((float)worldPos.z + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
				
				this.worldObj.setBlock(worldPos.x, worldPos.y+1, worldPos.z, Blocks.wheat,0,2);
				blockCrop = this.worldObj.getBlock(worldPos.x, worldPos.y+1, worldPos.z);
			}
			
			if( blockCrop instanceof IGrowable){
				ItemStack iStack=new ItemStack(Items.dye,64,15);
				ItemDye.applyBonemeal(iStack, worldObj, worldPos.x, worldPos.y+1, worldPos.z, Minecraft.getMinecraft().thePlayer);
			}
			
			if( blockCrop instanceof IGrowable){
				int meta = worldObj.getBlockMetadata(worldPos.x, worldPos.y+1, worldPos.z);
				if( meta==7){
					final ArrayList<ItemStack> drops = blockCrop.getDrops(worldObj, worldPos.x, worldPos.y+1, worldPos.z, meta,0);
					worldObj.setBlockToAir(worldPos.x, worldPos.y+1, worldPos.z);
					for(ItemStack iStack:drops){
						if (iStack != null){
	                        float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
	                        float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
	                        float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
	
	                                                        
	                            EntityItem entityitem = new EntityItem(worldObj, (double)((float)worldPos.x+ f), (double)((float)worldPos.y+1 + f1), (double)((float)worldPos.z + f2), iStack);
	
	                            float f3 = 0.05F;
	                            entityitem.motionX = (double)((float)this.field_149933_a.nextGaussian() * f3);
	                            entityitem.motionY = (double)((float)this.field_149933_a.nextGaussian() * f3 + 0.2F);
	                            entityitem.motionZ = (double)((float)this.field_149933_a.nextGaussian() * f3);
	                            worldObj.spawnEntityInWorld(entityitem);
	                    }
					}
				}
			}
			
			this.markDirty();
		}
		return true;
	}

	@Override
	protected void doCommit(int serverTick) {
		this.localCS=null;
		this.local=null;
		if(maxStepReached()){
			setStep(0);
		}
		System.out.println("doCommit:"+serverTick);
	}

	@Override
	protected void doRollback(int serverTick) {		
		decStep();
		this.localCS=null;
		this.local=null;
		//setupCS();
		System.out.println("doRollback:"+serverTick);
	}

	@Override
	protected boolean renderScene(int clientTick) {
		setupCS();
		System.out.println("renderScene:"+clientTick);
		return true;
	}

	@Override
	protected boolean renderSceneOut(int clientTick) {
		setupCS();
		System.out.println("renderSceneOut:"+clientTick);
		return true;
	}

	@Override
	protected boolean renderCooldown(int clientTick) {
		setupCS();
		System.out.println("renderCooldown:"+clientTick);
		return true;
	}

	@Override
	protected boolean renderFailure(int clientTick) {
		setupCS();
		System.out.println("renderFailure:"+clientTick);
		return true;
	}
	
	@Override
	protected int stageDuration(Lifecycle stage2) {
		return 16;
	}
	
}
