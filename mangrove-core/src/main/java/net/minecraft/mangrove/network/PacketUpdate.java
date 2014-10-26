/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.network;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketUpdate extends AbstractPacket {

	public int posX;
	public int posY;
	public int posZ;
	public PacketPayload payload;

//	private int packetId;

	public PacketUpdate() {
	}

	public PacketUpdate(PacketPayload payload) {
		this( 0, 0, 0, payload);
	}

	public PacketUpdate(int posX, int posY, int posZ, PacketPayload payload) {
		this();

		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;

		this.payload = payload;
	}
	
	public PacketUpdate(TileEntity tileEntity,PacketPayload packetPayload) {
		this(tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord, packetPayload);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(posX);
		buffer.writeInt(posY);
		buffer.writeInt(posZ);
		this.payload.writeData(buffer);
	}
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		posX = buffer.readInt();
		posY = buffer.readInt();
		posZ = buffer.readInt();

		payload = new PacketPayload();

		if (payload != null) {
			payload.readData(buffer);
		}	
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		final World world = player.worldObj;

        final TileEntity te = world.getTileEntity(posX,posY,posZ);
        if (te instanceof ITileUpdatable) {
			ITileUpdatable tupdatable = (ITileUpdatable) te;
			try {
				tupdatable.handleUpdatePacket(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		final World world = player.worldObj;

        final TileEntity te = world.getTileEntity(posX,posY,posZ);
        if (te instanceof ITileUpdatable) {
			ITileUpdatable tupdatable = (ITileUpdatable) te;
			try {
				tupdatable.handleUpdatePacket(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//	@Override
//	public void writeData(ByteBuf data) {
//		data.writeByte(packetId);
//		data.writeInt(posX);
//		data.writeInt(posY);
//		data.writeInt(posZ);
//
//		if (payload != null) {
//			payload.writeData(data);
//		} else {
//			data.writeByte(0);
//		}
//	}
//
//	@Override
//	public void readData(ByteBuf data) {
//		packetId = data.readByte();
//		posX = data.readInt();
//		posY = data.readInt();
//		posZ = data.readInt();
//
//		payload = new PacketPayload();
//
//		if (payload != null) {
//			payload.readData(data);
//		}
//	}
//
//	@Override
//	public int getID() {
//		return packetId;
//	}
}

