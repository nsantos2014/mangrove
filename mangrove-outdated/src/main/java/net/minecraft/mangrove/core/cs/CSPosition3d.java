package net.minecraft.mangrove.core.cs;

import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

public class CSPosition3d extends CSPoint3d{
	public ForgeDirection direction=ForgeDirection.UNKNOWN;

	public CSPosition3d() {
	    super();
	}
	public CSPosition3d(CSPosition3i position) {
		this(position.x,position.y,position.z,position.direction);
	}

	public CSPosition3d(double x, double y, double z) {
		super(x, y, z);
	}

	public CSPosition3d(double x, double y, double z, ForgeDirection direction) {
		super(x, y, z);
		this.direction = direction;
	}

	public CSPosition3d(CSPosition3d position) {
		this(position.x,position.y,position.z,position.direction);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.direction=ForgeDirection.valueOf(tag.getString("direction"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setString("direction",this.direction.name());
		
	}

	@Override
	public JsonObject getPacketData() {
		JsonObject data = super.getPacketData();
		data.addProperty("direction",this.direction.name());
		return data;		
	}

	@Override
	public void setPacketData(JsonObject data) {
		super.setPacketData(data);
		this.direction=ForgeDirection.valueOf(data.get("direction").getAsString());
	}

	public void set(CSPosition3d pos) {
		super.set(pos);
		this.direction=pos.direction;
	}
    @Override
    public String toString() {
        return "CSPosition3d [direction=" + direction + ", x=" + x + ", y=" + y + ", z=" + z + "]";
    }
	
	
	
}
