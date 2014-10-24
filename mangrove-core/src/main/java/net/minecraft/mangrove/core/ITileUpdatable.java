package net.minecraft.mangrove.core;

import java.io.IOException;

import net.minecraft.mangrove.network.PacketPayload;
import net.minecraft.mangrove.network.PacketUpdate;

public interface ITileUpdatable {

	PacketUpdate getPacketUpdate(); 
	
	void handleUpdatePacket(PacketUpdate packet) throws IOException;
}
