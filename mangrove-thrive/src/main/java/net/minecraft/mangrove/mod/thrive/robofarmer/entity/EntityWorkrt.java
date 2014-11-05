package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.mangrove.core.CoreConstants;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.core.proxy.FactoryProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class EntityWorkrt{
	public MoveType moveType=MoveType.Setup;
	//public ActionType armState=ActionType.Move;
	private double headStep=0.05;
	private double headLength = 0.05;
	private int headAim = 10;
	
	private double armLength = 0.05;
	private int armAim = 3;
	private double armStep=0.05;
	
	private EntityBlock head;
	private EntityBlock arm;
	
	private void createHead(World worldObj,int xCoord,int yCoord,int zCoord,ForgeDirection dir) {
		if (head == null) {
			head = FactoryProxy.proxy.newPumpTube(worldObj);
			
			setTubePosition(xCoord,yCoord,zCoord,dir);
			worldObj.spawnEntityInWorld(head);			
		}
		if (arm== null) {
			arm = FactoryProxy.proxy.newPumpTube(worldObj);
			setArmPosition(xCoord,yCoord,zCoord,dir);
			worldObj.spawnEntityInWorld(arm);
		}
	}
	
//	private void createArm(World worldObj,int xCoord,int yCoord,int zCoord,ForgeDirection dir) {
//		
//	}
	
	private void setTubePosition(double xCoord,double yCoord,double zCoord,ForgeDirection direction) {
		if (head != null) {
			head.iSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
			head.jSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
			head.kSize = headLength;

			head.setPosition(xCoord + CoreConstants.PIPE_MIN_POS, yCoord + CoreConstants.PIPE_MIN_POS,zCoord-headLength);
			
		}
	}
	
	private void setArmPosition(double xCoord,double yCoord,double zCoord,ForgeDirection direction) {
		if (arm != null) {
			arm.iSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
			arm.jSize = armLength;
			arm.kSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;

			arm.setPosition(xCoord + CoreConstants.PIPE_MIN_POS, yCoord+1-armLength,zCoord+ CoreConstants.PIPE_MIN_POS);
		}
	}
	

	public void destroyTube(World worldObj) {
		if (head != null) {
			destroyArm(worldObj);
			//CoreProxy.proxy.removeEntity(tube);
			worldObj.removeEntity(head);
			head = null;
			headLength = Double.NaN;
			headAim = 0;
			
		}
	}
	public void destroyArm(World worldObj) {
		if (arm != null) {
			//CoreProxy.proxy.removeEntity(tube);
			worldObj.removeEntity(arm);
			arm = null;
			armLength = Double.NaN;
			armAim = 0;
		}
	}	
	public void readFromNBT(NBTTagCompound tag){
        
        moveType=MoveType.values()[tag.getInteger("moveType")];
//        armState=ActionType.values()[tag.getInteger("armState")];
        
        headAim = tag.getInteger("headAim");
		headLength = tag.getFloat("headLength");
		headStep = tag.getFloat("headStep");	

		armAim = tag.getInteger("armAim");
		armLength = tag.getFloat("armLength");
		armStep = tag.getFloat("armStep");
    }

    public void writeToNBT(NBTTagCompound tag){
        tag.setInteger("moveType", moveType.ordinal());
//        tag.setInteger("armState", armState.ordinal());
        
        tag.setFloat("headLength",(float)headLength);
        tag.setFloat("headStep",(float)headStep);
        tag.setInteger("headAim", headAim);
        
        tag.setFloat("armLength",(float)armLength);
        tag.setFloat("armStep",(float)armStep);
        tag.setInteger("armAim", armAim);
    }
	
	public static enum MoveType{
		Setup,
		Expanding,
		Contracting,
		StopExpanding,
		StopContracting,
		Idle;
	}
	
	public boolean move(World worldObj, int xCoord, int yCoord, int zCoord,ForgeDirection dir) {
		
		if (head == null || arm==null) {		
			createHead(worldObj,xCoord,yCoord,zCoord,dir);    		
//			createArm(worldObj,xCoord,yCoord,zCoord,dir);
			return true;
		}
		if( moveType==MoveType.Setup && headLength<1){
			headLength = headLength + headStep;
			setTubePosition(xCoord,yCoord,zCoord,dir);
			return true;
		}
		if( moveType==MoveType.Setup && armLength<armAim){
			armLength = armLength + armStep*5;
			setArmPosition(xCoord,yCoord,zCoord-1,dir);
			return true;
		}
		if( moveType==MoveType.Expanding && headLength<headAim){
			
			headLength = headLength + headStep;
			setTubePosition(xCoord,yCoord,zCoord,dir);
			setArmPosition(xCoord,yCoord,zCoord-headLength,dir);
			return true;
		}
		
		if( moveType==MoveType.Contracting && headLength>1){
			headLength = headLength - headStep*4;
			setTubePosition(xCoord,yCoord,zCoord,dir);
			setArmPosition(xCoord,yCoord,zCoord-headLength,dir);
			return true;
		}
		
		if( moveType==MoveType.Setup){
			moveType=MoveType.Expanding;
//			return false;
		}
		if( moveType==MoveType.Expanding){
			moveType=MoveType.StopExpanding;
//			return true;
		}
//		if( moveType==MoveType.StopExpanding){
//			moveType=MoveType.Contracting;
//			return true;
//		}
//		if( moveType==MoveType.Contracting){
//			moveType=MoveType.StopContracting;
//			return true;
//		}
//		if( moveType==MoveType.StopContracting){
//			moveType=MoveType.Expanding;
//			return true;
//		}
		return false;
	}
	
	public void setPosition(World worldObj, int xCoord, int yCoord, int zCoord,	ForgeDirection dir) {
		if (head == null || arm==null) {		
			createHead(worldObj,xCoord,yCoord,zCoord,dir);    		
		}
		setTubePosition(xCoord,yCoord,zCoord,dir);
		setArmPosition(xCoord,yCoord,zCoord-headLength,dir);		
	}

	public int getXOffset(int xCoord, ForgeDirection forgeDirectionFromMetadata) {
		return xCoord;
	}
	public int getYOffset(int yCoord, ForgeDirection forgeDirectionFromMetadata) {
		return (int) (yCoord-Math.floor(armLength)-1);
	}
	public int getZOffset(int zCoord, ForgeDirection forgeDirectionFromMetadata) {
//		if( moveType==MoveType.Contracting){
//			return (int) Math.floor(zCoord-headLength);
//		}	
		return (int) Math.floor(zCoord-headLength)+1;
		
	}
		
	

	public boolean isSettingUp() {
		return this.moveType==MoveType.Setup;
	}
	public boolean isExpanding() {
		return this.moveType==MoveType.Expanding;
	}
	public boolean isContracting() {
		return this.moveType==MoveType.Contracting;
	}

	@Override
	public String toString() {
		return "EntityWorkrt [moveType=" + moveType 
				+ ", headStep=" + headStep + ", headLength=" + headLength
				+ ", headAim=" + headAim + ", armLength=" + armLength
				+ ", armAim=" + armAim + ", armStep=" + armStep + "]";
	}

	public JsonObject getData() {
		final JsonObject data=JSON.newObject();
		data.addProperty("moveType", moveType.toString());
//		data.addProperty("armState", armState.toString());
		
		data.addProperty("headAim", headAim);
		data.addProperty("headLength", headLength);
		data.addProperty("headStep", headStep);
		
		data.addProperty("armAim", armAim);
		data.addProperty("armLength", armLength);
		data.addProperty("armStep", armStep);
		return data;
	}

	public void setData(JsonObject data) {
		moveType=MoveType.valueOf(MoveType.class, data.get("moveType").getAsString());
//		armState=MoveType.valueOf(ActionType.class, data.get("armState").getAsString());
	
		headAim=data.get("headAim").getAsInt();
		headLength=data.get("headLength").getAsDouble();
		headStep=data.get("headStep").getAsDouble();
		
		armAim=data.get("armAim").getAsInt();
		armLength=data.get("armLength").getAsDouble();
		armStep=data.get("armStep").getAsDouble();
	}
	
}

