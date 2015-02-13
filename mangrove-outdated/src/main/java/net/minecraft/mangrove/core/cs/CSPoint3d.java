package net.minecraft.mangrove.core.cs;

import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

public class CSPoint3d{
	public double x=0,y=0,z=0;

	public CSPoint3d() {
	}
	
	public CSPoint3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void readFromNBT(NBTTagCompound tag){
		this.x=tag.getDouble("x");
		this.y=tag.getDouble("y");
		this.z=tag.getDouble("z");
	}

	public void writeToNBT(NBTTagCompound tag){
		tag.setDouble("x",this.x);
		tag.setDouble("y",this.y);
		tag.setDouble("z",this.z);
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

	public void set(CSPoint3d pos) {
		this.x=pos.x;
		this.y=pos.y;
		this.z=pos.z;
	}

    @Override
    public String toString() {
        return "CSPoint3d [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
	
	
}
