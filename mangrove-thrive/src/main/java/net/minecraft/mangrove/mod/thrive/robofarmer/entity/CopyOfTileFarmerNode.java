package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.CoreConstants;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.core.proxy.FactoryProxy;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.mangrove.network.TileUpdatePacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class CopyOfTileFarmerNode extends TileEntity implements ITileUpdatable{
	//private EntityBlock head;
	private CopyOfEntityWorkrt head=new CopyOfEntityWorkrt();
	
	//private ForgeDirection direction=ForgeDirection.UNKNOWN;
//	private double tubeStep=0.02;
//	private double tubePos = Double.NaN;
//	private int aimPos = 20;
	private int tick=0;
	
	private int prevX=xCoord;
	private int prevY=yCoord;
	private int prevZ=zCoord;
	
//	private State armState=State.Setup;
//	
//	enum State{
//		Setup,
//		Expanding,
//		Contracting,
//		Idle;
//	}
		
	public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        prevX = tag.getInteger("prevX");      
        prevY = tag.getInteger("prevY");
        prevZ = tag.getInteger("prevZ");
        
        //this.transferCooldown = tag.getInteger("TransferCooldown");
        
//        armState=State.values()[tag.getInteger("armState")];
//        aimPos = tag.getInteger("aimPos");
//		tubePos = tag.getFloat("tubePos");
//		tubeStep = tag.getFloat("tubeStep");
//		direction= ForgeDirection.getOrientation(tag.getInteger("direction"));
//		
		head.readFromNBT(tag);
    }

    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setInteger("prevX", prevX);
        tag.setInteger("prevY", prevY);
        tag.setInteger("prevZ", prevZ);
       // tag.setInteger("TransferCooldown", this.transferCooldown);
        
//        if (head != null) {
//        	tag.setFloat("tubePos", (float) head.posZ);
//		} else {
//			tag.setFloat("tubePos", zCoord);
//		}
//        tag.setInteger("armState", armState.ordinal());
//        tag.setFloat("tubePos",(float)tubePos);
//        tag.setFloat("tubeStep",(float)tubeStep);
//        tag.setInteger("aimPos", aimPos);
//        tag.setInteger("direction", direction.ordinal());
        head.writeToNBT(tag);
    }
    @Override
    public Block getBlockType() {
    	final Block blockType2 = super.getBlockType();
//    	System.out.println("Block Type:"+blockType2);
		return blockType2;
    }
    @Override
    public void updateEntity() {
    	tick++;
    	if( this.worldObj.isRemote){
    		int xOffset = head.getXOffset(xCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
    		int yOffset = head.getYOffset(yCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
    		int zOffset = head.getZOffset(zCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
    	
    		if( head.isMoving()){
    			head.setPosition(worldObj,xCoord,yCoord,zCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
    		}else if(head.isExpanding()){
    			if( head.isPlow()){
    				for( double x=0.1; x<1; x+=.1){
	    				for( double z=0.1; z<1; z+=.1){
	    					worldObj.spawnParticle("spell", xOffset+x, yOffset+1+0.1, zOffset+z, 0.5D, 1.0D, 0.5D);
	        			}	
	    			}
	    		}else if( head.isIrrigate()){
	    			worldObj.spawnParticle("splash", xOffset+0.25, yOffset+2-0.1, zOffset+0.25, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("splash", xOffset+0.25, yOffset+2-0.1, zOffset+0.75, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("splash", xOffset+0.50, yOffset+2-0.1, zOffset+0.50, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("splash", xOffset+0.75, yOffset+2-0.1, zOffset+0.25, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("splash", xOffset+0.75, yOffset+2-0.1, zOffset+0.75, 0.0D, 0.0D, 0.0D);
	    		}else if( head.isPlant()){
	    			worldObj.spawnParticle("spell", xOffset+0.25, yOffset+1+0.1, zOffset+0.25, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("spell", xOffset+0.25, yOffset+1+0.1, zOffset+0.75, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("spell", xOffset+0.50, yOffset+1+0.1, zOffset+0.50, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("spell", xOffset+0.75, yOffset+1+0.1, zOffset+0.25, 0.0D, 0.0D, 0.0D);
	    			worldObj.spawnParticle("spell", xOffset+0.75, yOffset+1+0.1, zOffset+0.75, 0.0D, 0.0D, 0.0D);
	    		}
    		}else if(head.isContracting()){
    			if( head.isFertilize()){
	    			worldObj.spawnParticle("happyVillager", xOffset+0.25, yOffset+1+0.1, zOffset+0.25,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("happyVillager", xOffset+0.25, yOffset+1+0.1, zOffset+0.75,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("happyVillager", xOffset+0.50, yOffset+1+0.1, zOffset+0.50,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("happyVillager", xOffset+0.75, yOffset+1+0.1, zOffset+0.25,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("happyVillager", xOffset+0.75, yOffset+1+0.1, zOffset+0.75,  0.5D, 1.0D, 0.5D);
	    		}else if( head.isHarvest()){
	    			worldObj.spawnParticle("mobSpell", xOffset+0.25, yOffset+1+0.1, zOffset+0.25,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("mobSpell", xOffset+0.25, yOffset+1+0.1, zOffset+0.75,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("mobSpell", xOffset+0.50, yOffset+1+0.1, zOffset+0.50,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("mobSpell", xOffset+0.75, yOffset+1+0.1, zOffset+0.25,  0.5D, 1.0D, 0.5D);
	    			worldObj.spawnParticle("mobSpell", xOffset+0.75, yOffset+1+0.1, zOffset+0.75,  0.5D, 1.0D, 0.5D);
	    		}
    		}    		
    		return;
    	}
    	if(head.isMoving()){
    		if(head.move(worldObj,xCoord,yCoord,zCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()))){    			    			
    			sendNetworkUpdate();
			//return;
    		}
    	}
    	    	
    	return ;
//		int xOffset = head.getXOffset(xCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
//		int yOffset = head.getYOffset(yCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
//		int zOffset = head.getZOffset(zCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
//		
////		worldObj.spawnParticle("smoke", (double)xOffset, (double)yOffset, (double)zOffset, 0.0D, 0.0D, 0.0D);
//		
//		if( xOffset!=prevX || yOffset!=prevY|| zOffset!=prevZ){
//			if(!head.isSettingUp()){
//				if( head.isExpanding()){
//					head.doPlow();
//				} else if( head.isContracting()){
//					head.doFertilize();
//				}
////				sendNetworkUpdate();
////				System.out.println("Execute : "
////					+" remote:"+this.worldObj.isRemote	
////					+ " x="+xOffset
////					+ " y="+yOffset
////					+ " z="+zOffset
////					+"  et="+head
////				);				
//			}			
//			this.prevX=xOffset;
//			this.prevY=yOffset;
//			this.prevZ=zOffset;
//			sendNetworkUpdate();
//		}
//		
//		if (this.tick % 32 == 0) {
//			final Block block1 = worldObj.getBlock(prevX, prevY+1, prevZ);
//			final Block block = worldObj.getBlock(prevX, prevY, prevZ);
//			System.out.println("Blocks:"+head.armState+":"+block+":"+block1);
//			if (head.isPlow()) {
//				if (block == Blocks.grass || block == Blocks.dirt) {
//					doPlow();					
//				}else{
//					System.out.println("Block not plowable:"+block+":"+block1);
//				}
//				head.doIrrigate();
//			} else if (head.isIrrigate()) {
//				if (block == Blocks.farmland) {
//					doIrrigate();
//				}else{
//					System.out.println("Block not irrigable:"+block+":"+block1);
//				}
//				head.doPlant();
//			} else if (head.isPlant()) {
//				final Block crop = worldObj.getBlock(prevX, prevY+1, prevZ);
//				if (block == Blocks.farmland && crop == Blocks.air) {
//					doPlant();
//				}
//				//head.doFertilize();
//				head.doMove();
//			} else if (head.isFertilize()) {
//				doFertilize();
//				head.doHarvest();
//			} else if (head.isHarvest()) {
//				doHarvest();
//				head.doMove();
//			}
//			sendNetworkUpdate();
////			if (this.worldObj.isRemote) {
//			this.markDirty();
////			}
//		}
//		if(this.tick % 16 != 0){
//    		System.out.println("Update Entity:"+this.worldObj+":"+this.worldObj.getBlock(xCoord, yCoord, zCoord));
//    	}
    }


	public void updateEntity2() {
		System.out.println("Update Entity:"+this.worldObj.isRemote);
		if(head.move(worldObj,xCoord,yCoord,zCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()))){
			System.out.println("Move Entity:"+this.worldObj.isRemote);
			sendNetworkUpdate();
			//return;
		}
//		
//		int xOffset = head.getXOffset(xCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
//		int yOffset = head.getYOffset(yCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
//		int zOffset = head.getZOffset(zCoord,BlockUtils.getForgeDirectionFromMetadata(getBlockMetadata()));
//		
//		if( xOffset!=prevX || yOffset!=prevY|| zOffset!=prevZ){
//			System.out.println("Execute : "
//				+" remote:"+this.worldObj.isRemote	
//				+ " x="+xOffset
//				+ " y="+yOffset
//				+ " z="+zOffset
//			);
//			if(!head.isSettingUp()){
//				final Block block=worldObj.getBlock(xOffset, yOffset, zOffset);
//				
//				if(block==Blocks.grass || block==Blocks.dirt){
//					head.doPlow();
//				}else if(block==Blocks.farmland){
//					head.doIrrigate();
//				}else{
//					final Block crop=worldObj.getBlock(xOffset, yOffset+1, zOffset);
//					if( crop.isAir(worldObj, xOffset, yOffset+1, zOffset)){
//						head.doPlant();
//					}else{
//						head.doFertilize();
//					}
//				}
//			}
//			this.prevX=xOffset;
//			this.prevY=yOffset;
//			this.prevZ=zOffset;
//			sendNetworkUpdate();			
//		}
//		if( head.isMove()){
//			return;
//		}
//		if (tubePos < 20 && tubePos > 0) {
//			tubePos = tubePos + tubeStep;
//			setTubePosition();
//			sendNetworkUpdate();
//			return;
//		} else {
//			tubeStep *=-1;
//			tubePos = tubePos + tubeStep;
//			//return;
//		}
		
//		this.tick++;
//		
//		if(this.tick % 16 != 0){
//			return;
//		}
//		
//		//boolean serverSide = this.worldObj != null && !this.worldObj.isRemote;
//		if (this.tick % 32 == 0){			
//			final Block block=worldObj.getBlock(prevX, prevY, prevZ);
//			if( head.isPlow()){
//				if(block==Blocks.grass || block==Blocks.dirt){
//					doPlow();
//				}
//              	head.doIrrigate();
//            }else if( head.isIrrigate()){
//            	if(block==Blocks.farmland){
//            		doIrrigate();
//            	}
//				head.doPlant();
//            }else if( head.isPlant() ){
//            	final Block crop=worldObj.getBlock(prevX, prevY+1, prevZ);
//            	if( block==Blocks.farmland && crop==Blocks.air){
//            		doPlant();
//            	}
//            	head.doFertilize();
//            }else if( head.isFertilize() ){
//            	doFertilize();
//            	head.doHarvest();
//            }else if( head.isHarvest() ){
//            	head.doMove();
//			}						
//			sendNetworkUpdate();
//			if( this.worldObj.isRemote){
//				this.markDirty();
//			}
//		}
//		
		
//		if (this.tick % 128==0){
//        	if(aimPos <=0){
//        		aimPos=20;
//	        	/*final Block block = worldObj.getBlock(xCoord, yCoord-1, zCoord);
//	        	if( block==Blocks.air){
//	        		aimPos = yCoord-1;
//	        	}*/
//        	}
//		}
	}

	
	private void doHarvest() {
		if( !this.worldObj.isRemote){
			final Block crop=worldObj.getBlock(prevX, prevY+1, prevZ);
			if( crop instanceof IGrowable){
				crop.breakBlock(worldObj, prevX, prevY+1, prevZ,crop,1);
			}
			this.markDirty();
		}
	}
	protected void doFertilize() {
		if( !this.worldObj.isRemote){
			final Block crop=worldObj.getBlock(prevX, prevY+1, prevZ);
			if( crop instanceof IGrowable){
				ItemStack iStack=new ItemStack(Items.dye,64,15);
				ItemDye.applyBonemeal(iStack, worldObj, prevX, prevY+1, prevZ, Minecraft.getMinecraft().thePlayer);
			}
			this.markDirty();
		}
	}

	protected void doPlant() {
		if( !this.worldObj.isRemote){
			worldObj.setBlock(prevX, prevY+1, prevZ, Blocks.wheat, 0, 2);
			this.markDirty();
		}
	}

	protected void doIrrigate() {
		//if( !this.worldObj.isRemote){
			this.worldObj.setBlockMetadataWithNotify(prevX, prevY, prevZ,7, 2);
			this.markDirty();
		//}
	}

	protected void doPlow() {
			//System.out.println("Plowing : "+this.worldObj.isRemote+":");
			final Block block1 = Blocks.farmland;
			//System.out.println("Replace with : "+block1);
			this.worldObj.playSoundEffect((double)((float)(prevX) + 0.5F), (double)((float)(prevY) + 0.5F), (double)((float)(prevZ) + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
//			if( !this.worldObj.isRemote){
			this.worldObj.setBlock(prevX, prevY, prevZ, block1);
//			}
			this.markDirty();
	}
	
	public void sendNetworkUpdate() {
		if (worldObj != null && !worldObj.isRemote) {
//			BuildCraftCore.instance.sendToPlayers(getUpdatePacket(), worldObj,
//					xCoord, yCoord, zCoord, DefaultProps.NETWORK_UPDATE_RANGE);
			NetBus.sendToClient(new TileEntityMessage(this));
		}
	}
	
	@Override
	public JsonObject getTilePacketData() {
		final JsonObject data = JSON.newObject();
		data.addProperty("prevX", prevX);
		data.addProperty("prevY", prevY);
		data.addProperty("prevZ", prevZ);
		data.add("head", head.getData());
		return data;
	}
	
	@Override
	public void handleClientUpdate(JsonObject data) throws IOException {
		//System.out.println("handleClientUpdate:"+data);
		prevX=data.get("prevX").getAsInt();
		prevY=data.get("prevY").getAsInt();
		prevZ=data.get("prevZ").getAsInt();
		head.setData(data.getAsJsonObject("head"));
	}
	
	@Override
	public void handleServerUpdate(JsonObject data) throws IOException {
		System.out.println("handleServerUpdate:"+data);
	}
	
////	@Override
////	public PacketUpdate getPacketUpdate() {
////		return new PacketUpdate(this,getPacketPayload());
////	}	
//	public PacketPayload getPacketPayload() {
//		PacketPayload payload = new PacketPayload(new PacketPayload.StreamWriter() {
//			@Override
//			public void writeData(ByteBuf buf) {
//				head.writeData(buf);
//				buf.writeInt(prevX);
//				buf.writeInt(prevY);
//				buf.writeInt(prevZ);
////				buf.writeInt(armState.ordinal());
////				buf.writeInt(aimPos);
////				buf.writeFloat((float) tubePos);
////				buf.writeFloat((float) tubeStep);
////				buf.writeInt(direction.ordinal());
////				buf.writeBoolean(powered);
//			}
//		});
//
//		return payload;
//	}
//	@Override
//	public void handleUpdatePacket(PacketUpdate packet) throws IOException {
//		PacketPayload payload = packet.payload;
//		ByteBuf data = payload.stream;
//		prevX = data.readInt();
//		prevY = data.readInt();
//		prevZ = data.readInt();
////		armState=State.values()[data.readInt()];
////		aimPos = data.readInt();
////		tubePos = data.readFloat();
////		tubeStep = data.readFloat();
////		direction=ForgeDirection.values()[data.readInt()];
//		head.readData(data);
//		System.out.println("handleUpdatePacket:"+head);
//	}

	 /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty(){
        super.markDirty();
    }
    
//    private void setTubePosition() {
//		if (head != null) {
//			head.iSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
//			head.jSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
//			head.kSize = tubePos;
//
//			head.setPosition(xCoord + CoreConstants.PIPE_MIN_POS, yCoord + CoreConstants.PIPE_MIN_POS,zCoord-tubePos);
//		}
//	}
    @Override
	public void invalidate() {
		super.invalidate();
		destroy();
	}

	@Override
	public void validate() {
		//tileBuffer = null;
		super.validate();
	}

	//@Override
	public void destroy() {
		//tileBuffer = null;
//		pumpLayerQueues.clear();
		head.destroyTube(worldObj);
	}
//	 private void createTube() {
//			if (head == null) {
//				head = FactoryProxy.proxy.newPumpTube(worldObj);
//
////				if (!Double.isNaN(tubePos)) {
////					head.posZ = tubePos;
////				} else {
////					head.posZ = zCoord;
////				}
//				tubeStep=Math.abs(tubeStep);
//				tubePos = tubeStep;
//				
//
////				if (aimPos == 0) {
////					aimPos = zCoord;
////				}
//
//				setTubePosition();
//
//				worldObj.spawnEntityInWorld(head);
//
//				if (!worldObj.isRemote) {
//					sendNetworkUpdate();
//				}
//			}
//		}
//
//		private void destroyTube() {
//			if (head != null) {
//				//CoreProxy.proxy.removeEntity(tube);
//				worldObj.removeEntity(head);
//				head = null;
//				tubePos = Double.NaN;
//				aimPos = 0;
//			}
//		}
}

