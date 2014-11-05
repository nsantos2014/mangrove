package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import java.io.IOException;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.Position;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.action.MoveAction;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.action.SetupAction;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.action.VoidServerExecuteAction;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.action.VoidServerUpdateAction;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.VoidBehavior;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.gson.JsonObject;

import cpw.mods.fml.relauncher.Side;

public class TileFarmerNode extends TileEntity implements ITileUpdatable {
	private static final VoidBehavior VOID_BEHAVIOR = new VoidBehavior();
	private static final VoidServerExecuteAction VOID_EXECUTE_ACTION = new VoidServerExecuteAction();
	private static final VoidServerUpdateAction VOID_UPDATE_ACTION = new VoidServerUpdateAction();
	private final Random field_149933_a = new Random();
	private int tick = 0;	
	private int step = 0;
	
	private Activity machineState = Activity.Setup;
	private OperationStatus operationStatus = OperationStatus.Start;
	private IBehaviour activeBehaviour = VOID_BEHAVIOR;
		
	// TODO Auto-generated method stub
	
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
//		if( tag.hasKey("position")){
//			position=new CSPosition3i();
//			position.readFromNBT(tag);
//		}else{
//			position=null;
//		}
		this.step=tag.getInteger("step");
		machineState = Activity.values()[tag.getInteger("machineState")];		
	}

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
//		if( position!=null){
//			final NBTTagCompound positionTag = new NBTTagCompound();
//			position.writeToNBT(tag);
//			tag.setTag("position", positionTag);
//		}else{
//			tag.removeTag("position");
//		}
		tag.setInteger("step", this.step);
		tag.setInteger("machineState", machineState.ordinal());		
	}

	@Override
	public Block getBlockType() {
		final Block blockType2 = super.getBlockType();

		return blockType2;
	}	

	@Override
	public void updateEntity() {

		tick++;
		if (this.worldObj.isRemote) {
			updateEntityClient();
		} else {
			updateEntityServer();
		}
	}

	protected void updateEntityClient() {
		if(  operationStatus==OperationStatus.Start || operationStatus==OperationStatus.End){
			final IBehaviour behaviour = VOID_BEHAVIOR;
			
			final CSPosition3i position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
			final CS localCS = CS.subSystem(position);
			final CSPosition3i local = new CSPosition3i();
			local.x=step-1;
			final CSPosition3i worldPos1 = localCS.toWorld(local);
			behaviour.init(worldObj, worldPos1);
			if( this.activeBehaviour != behaviour){
				behaviour.start();
				this.activeBehaviour = behaviour;
			}
			behaviour.execute();
			if(behaviour.hasStopped()){
				operationStatus = operationStatus==OperationStatus.End?OperationStatus.Update:OperationStatus.Execute;
				fireOperationStatusEvent(Side.CLIENT);
			}
		}
	}

	protected void updateEntityServer() {
		if (tick % 64 == 0) {
			final CSPosition3i position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
			final CS localCS = CS.subSystem(position);
			final CSPosition3i local = new CSPosition3i();		
			
			local.x=step+1;
			final CSPosition3i worldPos1 = localCS.toWorld(local);
			
			if( operationStatus==OperationStatus.Execute){
				IServerExecuteAction action=VOID_EXECUTE_ACTION; 										
							
				action.init(this.worldObj , worldPos1,step);
				action.start();
				do{ 
					action.execute();
				}while(!action.isStopped());				
				fireActionExecutedEvent(action.isSuccessfull());
			}else if( operationStatus==OperationStatus.Update){
				IServerUpdateAction action=VOID_UPDATE_ACTION; 										
				
				action.init(this.worldObj , worldPos1,machineState,step);
				action.prepare();
				do{ 
					action.update();
				}while(!action.isDone());
				if(action.isSuccessfull()){
					step=action.nextStep();
					machineState=action.nextActivity();
				}
				sendNetworkUpdate();
				operationStatus = OperationStatus.Start;
				fireOperationStatusEvent(Side.SERVER);
			}			
		}
	}

//	protected boolean executeAction() {
//		IAction action; 				
//		final CSPosition3i position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
//		final CS localCS = CS.subSystem(position);
//		final CSPosition3i local = new CSPosition3i();		
//		boolean result;
//		switch (machineState) {
//		case Setup:			
//								
//			local.x=step+1;
//			final CSPosition3i worldPos1 = localCS.toWorld(local);			
//			
//			action = new SetupAction();
//			action.init(this.worldObj , worldPos1);
//			action.start();
//			do{ 
//				action.execute();
//			}while(!action.hasStopped());
//			result=action.wasSuccessfull();
//			if( result) {
//				step++;
//			}
//			return result;
//		case Move:
//			
//			local.x=step+1;
//			final CSPosition3i worldPos2 = localCS.toWorld(local);			
//			
//			action = new MoveAction();			
//			action.init(this.worldObj , worldPos2);
//			action.start();
//			do{ 
//				action.execute();
//			}while(!action.hasStopped());
//			result=action.wasSuccessfull();
//			if( result) {
//				step=(step<10)?step+1:0;
//			}
//			return result;
//		case Plow:
//			executePlowAction();
//			break;
//		case Irrigate:
//			executeIrrigateAction();
//			break;
//		case Plant:
//			executePlantAction();
//			break;
//		case Fertilize:
//			executeFertilizeAction();
//			break;
//		case Harvest:
//			executeHarvestAction();
//			break;
//		}
//		return true;
//	}

	protected void executeHarvestAction() {
//		if (!this.worldObj.isRemote) {
//			final Block crop = worldObj.getBlock(prevX, prevY + 1, prevZ);
//			if (crop instanceof IGrowable) {
//				int meta = worldObj.getBlockMetadata(prevX, prevY + 1, prevZ);
//				final ArrayList<ItemStack> drops = crop.getDrops(worldObj, prevX, prevY + 1, prevZ, meta,0);
//				worldObj.setBlockToAir(prevX, prevY + 1, prevZ);
//				for(ItemStack iStack:drops){
//					if (iStack != null){
//                        float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//                        float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//                        float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//
//                                                        
//                            EntityItem entityitem = new EntityItem(worldObj, (double)((float)prevX + f), (double)((float)prevY+1 + f1), (double)((float)prevZ + f2), iStack);
//
//                            float f3 = 0.05F;
//                            entityitem.motionX = (double)((float)this.field_149933_a.nextGaussian() * f3);
//                            entityitem.motionY = (double)((float)this.field_149933_a.nextGaussian() * f3 + 0.2F);
//                            entityitem.motionZ = (double)((float)this.field_149933_a.nextGaussian() * f3);
//                            worldObj.spawnEntityInWorld(entityitem);
//                    }
//				}
//			}
//			this.markDirty();
//		}
	}

	protected void executeFertilizeAction() {
//		if (!this.worldObj.isRemote) {
//			final Block crop = worldObj.getBlock(prevX, prevY + 1, prevZ);
//			if (crop instanceof IGrowable) {
//				ItemStack iStack = new ItemStack(Items.dye, 64, 15);
//				ItemDye.applyBonemeal(iStack, worldObj, prevX, prevY + 1,
//						prevZ, Minecraft.getMinecraft().thePlayer);
//			}
//			this.markDirty();
//		}
	}

	protected void executePlantAction() {
//		if (!this.worldObj.isRemote) {
//			worldObj.setBlock(prevX, prevY + 1, prevZ, Blocks.wheat, 0, 2);
//			this.markDirty();
//		}
	}

	protected void executeIrrigateAction() {
//		 if( !this.worldObj.isRemote){
//		this.worldObj.setBlockMetadataWithNotify(prevX, prevY, prevZ, 7, 2);
//		this.markDirty();
//		 }
	}

	protected void executePlowAction() {
//		final Block block1 = Blocks.farmland;
//		this.worldObj.playSoundEffect((double) ((float) (prevX) + 0.5F),
//				(double) ((float) (prevY) + 0.5F),
//				(double) ((float) (prevZ) + 0.5F),
//				block1.stepSound.getStepResourcePath(),
//				(block1.stepSound.getVolume() + 1.0F) / 2.0F,
//				block1.stepSound.getPitch() * 0.8F);
//		 if( !this.worldObj.isRemote){
//			 System.out.println("Replace with : "+block1);
//			 this.worldObj.setBlock(prevX, prevY, prevZ, block1, 1, 2);
//		 }
//		this.markDirty();
	}

	public void fireOperationStatusEvent(final Side side) {
		if (side == Side.CLIENT) {
			final JsonObject evt = JSON.newObject();
			evt.addProperty("FireEvent", true);
			evt.addProperty("Side", "CLIENT");
			evt.addProperty("operationStatus", operationStatus.name());
			NetBus.sendToServer(new TileEntityMessage(this, evt));
		} else {
			final JsonObject evt = JSON.newObject();
			evt.addProperty("FireEvent", true);
			evt.addProperty("Side", "SERVER");
			evt.addProperty("operationStatus", operationStatus.name());
			NetBus.sendToClient(new TileEntityMessage(this, evt));
		}
	}

	//@SideOnly(Side.SERVER)
	public void fireActionExecutedEvent(boolean status) {
		operationStatus = status?OperationStatus.End:OperationStatus.Start;
		final JsonObject evt = JSON.newObject();
		evt.addProperty("FireEvent", true);
		evt.addProperty("Side", "SERVER");
		evt.addProperty("step", step);
		evt.addProperty("ActionSuccess", status);
		evt.addProperty("operationStatus", operationStatus.name());
		NetBus.sendToClient(new TileEntityMessage(this, evt));		
	}
	
	public void sendNetworkUpdate() {
		if (worldObj != null && !worldObj.isRemote) {
			NetBus.sendToClient(new TileEntityMessage(this));
		}
	}

	@Override
	public JsonObject getTilePacketData() {

		final JsonObject data = JSON.newObject();
//		if( position!=null){
//			data.add("position", position.getPacketData());
//		}else{
//			data.add("position", null);
//		}
		data.addProperty("step", this.step);
		data.addProperty("machineState", machineState.toString());		
		return data;
	}

	@Override
	public void handleClientUpdate(JsonObject data) throws IOException {
		System.out.println(this.worldObj + ": Client? handleClientUpdate:"
				+ machineState + ":" + data);
//		if( data.has("position")){
//			if(data.get("position").isJsonNull()){
//				this.position=null;
//			}else{
//				this.position=new CSPosition3i();
//				this.position.setPacketData(data.getAsJsonObject("position"));
//			}
//		}
		if( data.has("step")){
			if(data.get("step").isJsonNull()){
				this.step=0;
			}else{
				this.step=data.get("step").getAsInt();
			}
		}
		if (data.has("FireEvent")) {
			final OperationStatus operationStatusOld = operationStatus;
			operationStatus = OperationStatus.valueOf(OperationStatus.class,
					data.get("operationStatus").getAsString());
			System.out.println("Client Operation Status transition:"
					+ operationStatusOld + "->" + operationStatus);
//			if( operationStatus==OperationStatus.End){
			if(data.has("ExecutionSuccess") && !data.get("ExecutionSuccess").getAsBoolean()){
				System.out.println("Failed Execution");
				//operationStatus=OperationStatus.Execute;
			}
//			}
		} else {
			// System.out.println("handleClientUpdate:"+data);			
			machineState = Activity.valueOf(Activity.class,
					data.get("machineState").getAsString());
			
		}
	}

	@Override
	public void handleServerUpdate(JsonObject data) throws IOException {
		System.out.println(this.worldObj + ":Server? handleServerUpdate:"
				+ machineState + ":" + data);
		if (data.has("FireEvent")) {
			final OperationStatus operationStatusOld = operationStatus;
			operationStatus = OperationStatus.valueOf(OperationStatus.class,
					data.get("operationStatus").getAsString());
			System.out.println("Server Operation Status transition:"
					+ operationStatusOld + "->" + operationStatus);
		} else {
			System.out.println("handleServerUpdate:" + data);
		}

	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved
	 * to disk later - the game won't think it hasn't changed and skip it.
	 */
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		destroy();
	}

	@Override
	public void validate() {
		super.validate();
	}

	// @Override
	public void destroy() {
		
	}

}
