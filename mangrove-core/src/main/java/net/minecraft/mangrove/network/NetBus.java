package net.minecraft.mangrove.network;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;


public class NetBus {
	public static final PacketPipeline packetPipeline = new PacketPipeline();

	public static void initialize(){
		packetPipeline.initialise();		
	}
	
	public static void sendToAll(AbstractPacket message) {
		packetPipeline.registerPacket(message.getClass());
		packetPipeline.sendToAll(message);
	}

	public static void sendTo(AbstractPacket message, EntityPlayerMP player) {
		packetPipeline.registerPacket(message.getClass());
		packetPipeline.sendTo(message, player);
	}

	public static void sendToAllAround(AbstractPacket message, TargetPoint point) {
		packetPipeline.registerPacket(message.getClass());
		packetPipeline.sendToAllAround(message, point);
	}

	public static void sendToDimension(AbstractPacket message, int dimensionId) {
		packetPipeline.registerPacket(message.getClass());
		packetPipeline.sendToDimension(message, dimensionId);
	}

	public static void sendToServer(AbstractPacket message) {
		packetPipeline.registerPacket(message.getClass());
		packetPipeline.sendToServer(message);
	}

	
}
