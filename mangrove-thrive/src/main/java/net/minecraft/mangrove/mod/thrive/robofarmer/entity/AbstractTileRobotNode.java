package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import io.netty.channel.rxtx.RxtxChannelConfig.Stopbits;

import java.io.IOException;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.robofarmer.block.SystemUtils;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileRobotNode extends TileEntity implements
		ITileUpdatable {
	private Lifecycle stage = Lifecycle.Off;
	private int tick = 0;
	private int step = 0;
	
	private int maxStep = 1;
    protected CSPoint3i controlPosition;	
	
	public int getMaxStep() {
		return maxStep;
	}
	public void setMaxStep(int maxStep) {
		this.maxStep = maxStep>1?maxStep:1;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step<0?0:step>maxStep?maxStep:step;
	}
	public void incStep() {
		this.step=this.step>maxStep?maxStep:this.step+1;
	}
	public void decStep() {
		this.step=this.step<0?0:this.step-1;
	}
	
	public boolean maxStepReached(){
		return this.step>=maxStep;
	}
	
	@Override
	public final void updateEntity() {
	    final CSPoint3i point=new CSPoint3i(xCoord,yCoord,zCoord);
        this.controlPosition = SystemUtils.findFirstControl(worldObj, xCoord, yCoord, zCoord);
        if( controlPosition==null){
            doStop();
            return;
        } else {
            if( !MGThriveBlocks.farmer_kernel.isPowered(worldObj, controlPosition)){
                doStop();
                return;
            }else if(stage==Lifecycle.Off){
                doStart();
                return;
            }
        }
		tick++;		
		if (this.worldObj.isRemote) {
			switch (stage) {
			case RenderScene:
				if( tick % _stageDuration(stage)==0){
					if(renderScene(tick)){
						this.stage=Lifecycle.Execute;
						fireLifecycleEvent();	
					}
				}
				break;
			case RenderSceneOut:
				if( tick % _stageDuration(stage)==0){
					if(renderSceneOut(tick)) {
						this.stage=Lifecycle.Commit;
						fireLifecycleEvent();
					}
				}
				break;
			case RenderCooldown:
				if( tick % _stageDuration(stage)==0){
					if(renderCooldown(tick)){
						this.stage=Lifecycle.Init;
						fireLifecycleEvent();
					}
				}
				break;
			case RenderFailScene:
				if( tick % _stageDuration(stage)==0){
					if(renderFailure(tick)){
						this.stage=Lifecycle.Init;
						fireLifecycleEvent();
					}
				}
				break;
			default:
				break;
			}
			
		} else {
			//System.out.println("Stage="+stage);
			switch (stage) {
			case Init:
				if( tick % _stageDuration(stage)==0){
					if(doInit(tick)){
						this.stage=Lifecycle.RenderScene;
						fireLifecycleEvent();
					}else{
						this.stage=Lifecycle.Rollback;
						fireLifecycleEvent();
					}
				}
				break;
			case Execute:
				if( tick % _stageDuration(stage)==0){
					if(doExecute(tick)){
						this.stage=Lifecycle.RenderSceneOut;
						fireLifecycleEvent();
					}else{
						this.stage=Lifecycle.Rollback;
						fireLifecycleEvent();
					}				
				}
				break;
			case Commit:
				if( tick % _stageDuration(stage)==0){
					doCommit(tick);
					this.stage=Lifecycle.RenderCooldown;
					fireLifecycleEvent();
				}
				break;
			case Rollback:
				if( tick % _stageDuration(stage)==0){
					doRollback(tick);
					this.stage=Lifecycle.RenderFailScene;
					fireLifecycleEvent();
				}
				break;
			default:				
				if( tick % 128==0){
//					System.out.println("Server Side Stage="+stage);
					fireLifecycleEvent();					
				}
				break;
			}
		}
	}

	protected abstract boolean doInit(int serverTick);	
	
	private int _stageDuration(Lifecycle stage2) {
		int duration=stageDuration(stage2);
		return duration<1?1:duration;
	}
	protected int stageDuration(Lifecycle stage2) {
		return 0;
	}

	public void doStart(){
	    if( this.worldObj!=null && !this.worldObj.isRemote && this.stage==Lifecycle.Off){
	        this.stage=Lifecycle.Init;
	        fireLifecycleEvent();
	        markDirty();
	    }
	}
	public void doStop(){
        if( this.worldObj!=null && !this.worldObj.isRemote && this.stage!=Lifecycle.Off){
            this.stage=Lifecycle.Off;
            fireLifecycleEvent();
            markDirty();
        }
    }
	protected abstract boolean doExecute(int serverTick);
	protected abstract void doCommit(int serverTick);
	protected abstract void doRollback(int serverTick);

	protected abstract boolean renderScene(int clientTick);
	protected abstract boolean renderSceneOut(int clientTick);
	protected abstract boolean renderCooldown(int clientTick);
	protected abstract boolean renderFailure(int clientTick);

	protected final void fireLifecycleEvent() {
		if (this.worldObj.isRemote) {
			final JsonObject evt = JSON.newObject();
			evt.addProperty("FireEvent", true);
			evt.addProperty("Side", "CLIENT");
			evt.addProperty("stage", stage.name());
			evt.addProperty("step", step);
			NetBus.sendToServer(new TileEntityMessage(this, evt));
		} else {
			final JsonObject evt = JSON.newObject();
			evt.addProperty("FireEvent", true);
			evt.addProperty("Side", "SERVER");
			evt.addProperty("stage", stage.name());
			evt.addProperty("step", step);
			NetBus.sendToClient(new TileEntityMessage(this, evt));
		}
	}

	protected void handleClientEvent(JsonObject data) {
	}

	protected void handleServerEvent(JsonObject data) {
	}

	@Override
	public JsonObject getTilePacketData() {
		final JsonObject data = JSON.newObject();
		data.addProperty("step", this.step);
		data.addProperty("stage", stage.name());
		return data;
	}

	@Override
	public void handleClientUpdate(JsonObject data) throws IOException {
//		System.out.println(this.worldObj + ": Client? handleClientUpdate:"
//				+ stage + ":" + data);
		if (data.has("step")) {
			if (data.get("step").isJsonNull()) {
				this.step = 0;
			} else {
				this.step = data.get("step").getAsInt();
			}
		}
		if (data.has("stage")) {
			if (!data.get("stage").isJsonNull()) {
				this.stage = Lifecycle.valueOf(data.get("stage").getAsString());
			}
		}
		if (data.has("FireEvent")) {
			handleServerEvent(data);
		}
	}

	@Override
	public void handleServerUpdate(JsonObject data) throws IOException {
		if (data.has("stage")) {
			if (!data.get("stage").isJsonNull()) {
				this.stage = Lifecycle.valueOf(data.get("stage").getAsString());
			}
		}
		if (data.has("FireEvent")) {
			handleClientEvent(data);
		}
	}

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.step = tag.getInteger("step");
		this.stage = Lifecycle.valueOf(tag.getString("stage"));
	}

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("step", this.step);
		tag.setString("stage", stage.name());
		//System.out.println("Write to NBT : step="+this.step+" stage="+this.stage);
	}
	
	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		super.markDirty();
	}
}
