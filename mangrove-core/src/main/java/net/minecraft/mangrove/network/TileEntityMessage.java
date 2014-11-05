package net.minecraft.mangrove.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TileEntityMessage implements IMessage{
	private int packetId;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private JsonObject data;
	
	public TileEntityMessage() {	
	}
	public TileEntityMessage(TileEntity tile){
		this.packetId=0;
		this.xCoord=tile.xCoord;
		this.yCoord=tile.yCoord;
		this.zCoord=tile.zCoord;
		if (tile instanceof ITileUpdatable) {
			ITileUpdatable tileUpdatable = (ITileUpdatable) tile;
			this.data=tileUpdatable.getTilePacketData();
		}
	}
	public TileEntityMessage(TileEntity tile,JsonObject data){
		this.packetId=0;
		this.xCoord=tile.xCoord;
		this.yCoord=tile.yCoord;
		this.zCoord=tile.zCoord;
		this.data=data==null?JSON.newObject():data;
	}
	
	@Override
	public void fromBytes(ByteBuf buffer) {
		xCoord = buffer.readInt();
		yCoord = buffer.readInt();
		zCoord = buffer.readInt();

		final int len = buffer.readInt();
		byte[] bytes = new byte[len];
		buffer.readBytes(bytes);
		final String json = new String(bytes);
		data = JSON.fromJson(json);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(xCoord);
		buffer.writeInt(yCoord);
		buffer.writeInt(zCoord);

		final String json = JSON.toJson(data);

		buffer.writeInt(json.length());
		buffer.writeBytes(json.getBytes());
	}

	public void handleClientSide(EntityPlayer player) {
		final World world = player.worldObj;

		final TileEntity te = world.getTileEntity(xCoord, yCoord, zCoord);
		if (te instanceof ITileUpdatable) {
			final ITileUpdatable tu = (ITileUpdatable) te;
			try {
				tu.handleClientUpdate(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void handleServerSide(EntityPlayer player) {
		final World world = player.worldObj;

		final TileEntity te = world.getTileEntity(xCoord, yCoord, zCoord);
		if (te instanceof ITileUpdatable) {
			final ITileUpdatable tu = (ITileUpdatable) te;
			try {
				tu.handleServerUpdate(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
