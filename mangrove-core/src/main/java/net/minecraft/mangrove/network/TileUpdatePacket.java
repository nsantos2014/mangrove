package net.minecraft.mangrove.network;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileUpdatePacket extends AbstractPacket {
	private int packetId;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private JsonObject data;

	public TileUpdatePacket() {
	}
	public TileUpdatePacket(TileEntity tile){
		this.packetId=0;
		this.xCoord=tile.xCoord;
		this.yCoord=tile.yCoord;
		this.zCoord=tile.zCoord;
		if (tile instanceof ITileUpdatable) {
			ITileUpdatable tileUpdatable = (ITileUpdatable) tile;
			this.data=tileUpdatable.getTilePacketData();
		}
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(xCoord);
		buffer.writeInt(yCoord);
		buffer.writeInt(zCoord);

		final String json = JSON.toJson(data);

		buffer.writeInt(json.length());
		buffer.writeBytes(json.getBytes());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
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

	@Override
	public void handleServerSide(EntityPlayer player) {
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

}
