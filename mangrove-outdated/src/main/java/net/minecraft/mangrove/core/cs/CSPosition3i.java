package net.minecraft.mangrove.core.cs;

import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonObject;

public class CSPosition3i extends CSPoint3i{
	public ForgeDirection direction=ForgeDirection.UNKNOWN;

	public CSPosition3i() {
		super();
	}

	public CSPosition3i(int x, int y, int z) {
		super(x, y, z);
	}

	public CSPosition3i(int x, int y, int z, ForgeDirection direction) {
		super(x, y, z);
		this.direction = direction;
	}

	public CSPosition3i(CSPosition3i position) {
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

	public void set(CSPosition3i pos) {
		super.set(pos);
		this.direction=pos.direction;
	}

    @Override
    public String toString() {
        return "CSPosition3i [direction=" + direction + ", x=" + x + ", y=" + y + ", z=" + z + "]";
    }
	
	
	
}
