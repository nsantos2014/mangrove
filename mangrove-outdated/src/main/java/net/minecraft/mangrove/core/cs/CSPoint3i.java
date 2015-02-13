package net.minecraft.mangrove.core.cs;

import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CSPoint3i other = (CSPoint3i) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CSPoint3i [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
	
}
