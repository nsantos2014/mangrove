package net.minecraft.mangrove.core.cs;

import com.google.gson.JsonObject;

import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.nbt.NBTTagCompound;

public class CSPoint3i{
	public int x=0,y=0,z=0;

	public CSPoint3i() {
	}
	
	public CSPoint3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void readFromNBT(NBTTagCompound tag){
		this.x=tag.getInteger("x");
		this.y=tag.getInteger("y");
		this.z=tag.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound tag){
		tag.setInteger("x",this.x);
		tag.setInteger("y",this.y);
		tag.setInteger("z",this.z);
	}
	
	public JsonObject getPacketData() {
		final JsonObject data = JSON.newObject();
		data.addProperty("x", this.x);
		data.addProperty("y", this.y);
		data.addProperty("z", this.z);		
		return data;
	}
	public void setPacketData(JsonObject data){
		this.x = data.get("x").getAsInt();
		this.y = data.get("y").getAsInt();
		this.z = data.get("z").getAsInt();
	}

	public void set(CSPoint3i pos) {
		this.x=pos.x;
		this.y=pos.y;
		this.z=pos.z;
	}
}
